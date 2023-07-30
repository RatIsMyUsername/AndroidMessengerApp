package ge.rmenagharishvili.messenger.home_users

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ge.rmenagharishvili.messenger.chat.Message
import ge.rmenagharishvili.messenger.fastToast
import ge.rmenagharishvili.messenger.user.Friend
import ge.rmenagharishvili.messenger.user.User

class Repository(private val context: Context) {
    private var storageRoot: StorageReference = FirebaseStorage.getInstance().reference
    private var dbRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var authService: FirebaseAuth = FirebaseAuth.getInstance()
    companion object{
        const val CHILD_NAME_CHATS = "chats"
        const val CHILD_NAME_MESSAGES = "messages"
        const val CHILD_NAME_FRIENDS = "Friends"
    }

    fun getCurrentUserId(): String {
        return authService.currentUser!!.uid
    }

    fun getPfpUrlFor(userId: String, callback: (Uri?) -> Unit){
        storageRoot.child("Users/${userId}").downloadUrl.addOnSuccessListener { uri ->
            callback(uri)
        }.addOnFailureListener {
            fastToast(context, "failed to load pfp for $userId")
            callback(null)
        }
    }

    fun getFriendIds(callback: (MutableList<Friend>) -> Unit){
        dbRoot.child(CHILD_NAME_FRIENDS).child(getCurrentUserId())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var friends = mutableListOf<Friend>()
                    for (postSnapshot in snapshot.children) {
                        val friend = postSnapshot.getValue(Friend::class.java) as Friend
                        friends.add(friend)
                    }
                    callback(friends)
                }

                override fun onCancelled(error: DatabaseError) {
                    println("ERROR")
                }
            })
    }
}