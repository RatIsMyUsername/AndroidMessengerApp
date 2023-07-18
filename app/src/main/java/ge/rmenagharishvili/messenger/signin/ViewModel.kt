package ge.rmenagharishvili.messenger.signin

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: Repository

    init {
        repo = Repository(application.applicationContext)
    }

    fun login(nickname: String, pass: String, callback: (Unit) -> Unit) {
        repo.login(nickname, pass, callback)
    }
}