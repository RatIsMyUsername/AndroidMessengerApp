package ge.rmenagharishvili.messenger.signup

class ViewModel(): androidx.lifecycle.ViewModel() {
    fun registerNew(nickname: String, pass: String, occupation: String){
        println(nickname)
    }
}