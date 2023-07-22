package ge.rmenagharishvili.messenger.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: Repository

    init {
        repo = Repository(application.applicationContext)
    }

    fun getCurrentUserId() : String? {
        return repo.getCurrentUserId()
    }

    fun sendMessage(senderId: String, receiverId: String, message: Message): Boolean {
        repo.sendMessage(senderId, receiverId, message)
        return true
    }
}