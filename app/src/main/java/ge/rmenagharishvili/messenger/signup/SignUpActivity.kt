package ge.rmenagharishvili.messenger.signup

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ge.rmenagharishvili.messenger.databinding.ActivitySignUpBinding
import ge.rmenagharishvili.messenger.validFields

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private val viewModel: ViewModel by lazy {
        ViewModelProvider(this, ViewModelsFactory(application))[ViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener{
            val nickname = binding.etNickname.text.toString()
            val pass = binding.etPassword.text.toString()
            val occupation = binding.etWhatIDo.text.toString()
            if (validFields(this, nickname,pass,occupation)){
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