package ge.rmenagharishvili.messenger.signup

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ge.rmenagharishvili.messenger.databinding.ActivitySignUpBinding
import ge.rmenagharishvili.messenger.fastToast

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private val viewModel: ViewModel by lazy {
        ViewModelProvider(this, ViewModelsFactory(application))[ViewModel::class.java]
    }

    companion object{
        const val PASS_MIN_LENGTH = 6
        const val MESSAGE_INVALID_PASS = "input at least "+ PASS_MIN_LENGTH + "chars for password"
        const val MESSAGE_INVALID_NICKNAME = "you must have a nickname"
        const val MESSAGE_INVALID_OCCUPATION = "you must have a job"
    }

    private fun validFields(nickname: String, pass: String, occupation: String): Boolean{
        if(nickname.isEmpty()){
            fastToast(this, MESSAGE_INVALID_NICKNAME)
            return false
        }
        if(pass.length < PASS_MIN_LENGTH){
            fastToast(this,MESSAGE_INVALID_PASS)
            return false
        }
        if(occupation.isEmpty()){
            fastToast(this, MESSAGE_INVALID_OCCUPATION)
            return false
        }
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener{
            val nickname = binding.etNickname.text.toString()
            val pass = binding.etPassword.text.toString()
            val occupation = binding.etWhatIDo.text.toString()
            if (validFields(nickname,pass,occupation)){
                viewModel.registerNew(nickname,pass,occupation)
            }
        }
    }
}

class ViewModelsFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModel::class.java)) {
            return ViewModel(Repository(app.applicationContext)) as T
        }
        throw IllegalArgumentException("no vm")
    }
}