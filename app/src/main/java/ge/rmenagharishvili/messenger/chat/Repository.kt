package ge.rmenagharishvili.messenger.chat

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Repository(private val context: Context) {
    private var authService: FirebaseAuth = FirebaseAuth.getInstance()
    private var dbRoot: DatabaseReference = FirebaseDatabase.getInstance().reference

    companion object {
        const val CHILD_NAME_USERS = "USERS"
        const val CHILD_NAME_CHATS = "chats"
        const val CHILD_NAME_MESSAGES = "messages"
    }

    fun getCurrentUserId(): String? {
        return authService.currentUser?.uid
    }

    fun sendMessage(senderId: String, receiverId: String, message: Message) {
        val senderRoom = senderId + receiverId
        val receiverRoom = receiverId + senderId

        dbRoot.child(CHILD_NAME_CHATS).child(senderRoom).child(CHILD_NAME_MESSAGES).push()
            .setValue(message).addOnSuccessListener {
                dbRoot.child(CHILD_NAME_CHATS).child(receiverRoom).child(CHILD_NAME_MESSAGES).push()
                    .setValue(message)
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
}