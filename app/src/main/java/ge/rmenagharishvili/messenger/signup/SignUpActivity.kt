package ge.rmenagharishvili.messenger.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ge.rmenagharishvili.messenger.databinding.ActivitySignUpBinding
import ge.rmenagharishvili.messenger.validFields

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ViewModel::class.java]

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
