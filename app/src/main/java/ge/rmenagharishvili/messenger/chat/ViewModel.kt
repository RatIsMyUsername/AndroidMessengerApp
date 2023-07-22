package ge.rmenagharishvili.messenger.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.hdodenhof.circleimageview.CircleImageView

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: Repository

    init {
        repo = Repository(application.applicationContext)
    }

    fun getCurrentUserId(): String? {
        return repo.getCurrentUserId()
    }

    fun sendMessage(senderId: String, receiverId: String, message: Message): Boolean {
        repo.sendMessage(senderId, receiverId, message)
        return true
    }

    fun updateMessageList(
        senderId: String,
        receiverId: String,
        messageList: ArrayList<Message>,
        successCallback: (Unit) -> Unit,
        failCallback: (Unit) -> Unit
    ) {
        repo.updateMessageList(senderId, receiverId, messageList, successCallback, failCallback)
    }

    fun loadPicture(uid: String, imageView: CircleImageView) {
        repo.loadPicture(uid, imageView)
    }
}