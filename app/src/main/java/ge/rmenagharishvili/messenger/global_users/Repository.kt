package ge.rmenagharishvili.messenger.global_users

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ge.rmenagharishvili.messenger.fastToast
import ge.rmenagharishvili.messenger.user.User


class Repository(private val context: Context) {
    private var authService: FirebaseAuth = FirebaseAuth.getInstance()
    private var dbRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var storageRoot: StorageReference = FirebaseStorage.getInstance().reference

    companion object {
        const val CHILD_NAME_USERS = "USERS"
        const val STOP_CODE = "\uf8ff"
    }

    fun getUsers(searchWith: String, callback: (MutableList<User>) -> Unit) {
        dbRoot.child(CHILD_NAME_USERS).orderByChild("nickname").startAt(searchWith).endAt(searchWith + STOP_CODE).get().addOnSuccessListener {
            val res = mutableListOf<User>()
            for(entry in it.children){
                if(entry.key.equals(authService.currentUser?.uid)){
                    continue
                }
                val retrievedUser: User = entry.getValue(User::class.java) as User
                retrievedUser.uid = entry.key
                res.add(retrievedUser)
            }
            callback(res)
        }.addOnFailureListener {
            fastToast(context,"could not retrieve user list")
            Log.e("server error when retrieving users", it.toString())
        }
    }

    fun getPfpUrlFor(userId: String, callback: (Uri) -> Unit){
        storageRoot.child("Users/${userId}").downloadUrl.addOnSuccessListener { uri ->
            callback(uri)
        }.addOnFailureListener {
            fastToast(context, "failed to load pfp for $userId")
        }
    }

}