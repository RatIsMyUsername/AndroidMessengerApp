package ge.rmenagharishvili.messenger.signin

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ge.rmenagharishvili.messenger.databinding.ActivitySignInBinding
import ge.rmenagharishvili.messenger.signup.SignUpActivity
import ge.rmenagharishvili.messenger.validFields

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    private val viewModel: ViewModel by lazy {
        ViewModelProvider(this, ViewModelsFactory(application))[ViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // start sign up activity if the user clicks the sign up button
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignIn.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            val pass = binding.etPassword.text.toString()
            if(validFields(this,nickname,pass,null)){
                viewModel.login(nickname,pass)
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