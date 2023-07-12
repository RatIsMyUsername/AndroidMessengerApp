package ge.rmenagharishvili.messenger.signup

class ViewModel(private val repo: Repository): androidx.lifecycle.ViewModel() {
    fun registerNew(nickname: String, pass: String, occupation: String){
        repo.registerNew(nickname,pass,occupation)
    }
}