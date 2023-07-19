package ge.rmenagharishvili.messenger.global_users

import android.app.Application
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
}