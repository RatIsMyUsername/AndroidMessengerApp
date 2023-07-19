package ge.rmenagharishvili.messenger.global_users

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import ge.rmenagharishvili.messenger.user.User

class ViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: Repository

    init {
        repo = Repository(application.applicationContext)
    }

    fun getUsers(searchWith: String, callback: (MutableList<User>) -> Unit) {
        repo.getUsers(searchWith, callback)
    }

    fun getPfpUrlFor(userId: String, callback: (Uri) -> Unit){
        repo.getPfpUrlFor(userId,callback)
    }
}