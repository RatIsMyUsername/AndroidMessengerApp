package ge.rmenagharishvili.messenger.chat

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import ge.rmenagharishvili.messenger.user.Friend
import ge.rmenagharishvili.messenger.user.User

class Repository(private val context: Context) {
    private var authService: FirebaseAuth = FirebaseAuth.getInstance()
    private var dbRoot: DatabaseReference = FirebaseDatabase.getInstance().reference

    companion object {
        const val CHILD_NAME_USERS = "USERS"
        const val CHILD_NAME_CHATS = "chats"
        const val CHILD_NAME_MESSAGES = "messages"
        const val CHILD_NAME_FRIENDS = "Friends"
    }

    fun getCurrentUserId(): String? {
        return authService.currentUser?.uid
    }

    fun sendMessage(sender: User, receiver: User, message: Message) {
        val senderId = sender.uid!!
        val receiverId = receiver.uid!!
        val senderRoom = senderId + receiverId
        val receiverRoom = receiverId + senderId

        dbRoot.child(CHILD_NAME_CHATS).child(senderRoom).child(CHILD_NAME_MESSAGES).push()
            .setValue(message).addOnSuccessListener {
                dbRoot.child(CHILD_NAME_CHATS).child(receiverRoom).child(CHILD_NAME_MESSAGES).push()
                    .setValue(message).addOnSuccessListener {
                        dbRoot.child(CHILD_NAME_FRIENDS).child(receiverId).child(senderId).setValue(Friend(nickname=sender.nickname, last_message = message.messageText, uid = senderId,occupation=sender.occupation, last_message_time = message.timeMillis))
                        dbRoot.child(CHILD_NAME_FRIENDS).child(senderId).child(receiverId).setValue(Friend(nickname = receiver.nickname, last_message = message.messageText, uid = receiverId, occupation=receiver.occupation, last_message_time = message.timeMillis))
                    }
            }
    }

    fun updateMessageList(
        senderId: String,
        receiverId: String,
        messageList: ArrayList<Message>,
        successCallback: (Unit) -> Unit,
        failCallback: (Unit) -> Unit
    ) {
        val senderRoom = senderId + receiverId
        dbRoot.child(CHILD_NAME_CHATS).child(senderRoom).child(CHILD_NAME_MESSAGES)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    successCallback(Unit)
                }

                override fun onCancelled(error: DatabaseError) {
                    failCallback(Unit)
                }
            })
    }

    fun loadPicture(uid: String, imageView: CircleImageView) {
        val storageReference =
            FirebaseStorage.getInstance().reference.child("Users/${uid}")
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get().load(uri).into(imageView)
        }.addOnFailureListener {}
    }

    fun fillUser(id: String, callback: (User?) -> Unit){
        dbRoot.child(CHILD_NAME_USERS).child(id).get().addOnSuccessListener {
            var user = it.getValue(User::class.java) as User
            user.uid = id
            callback(user)
        }.addOnFailureListener {
            callback(null)
        }
    }
}