package ge.rmenagharishvili.messenger.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ViewModel(application: Application) : AndroidViewModel(application){

    private val repo: Repository
    init {
        repo = Repository(application.applicationContext)
    }

    fun registerNew(nickname: String, pass: String, occupation: String){
        repo.registerNew(nickname,pass,occupation)
    }
}