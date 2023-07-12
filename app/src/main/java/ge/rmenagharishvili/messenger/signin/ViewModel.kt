package ge.rmenagharishvili.messenger.signin

class ViewModel(private val repo: Repository): androidx.lifecycle.ViewModel() {
    fun login(nickname: String, pass: String){
        repo.login(nickname,pass)
    }
}