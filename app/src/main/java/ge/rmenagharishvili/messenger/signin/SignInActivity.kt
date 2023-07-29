package ge.rmenagharishvili.messenger.signin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ge.rmenagharishvili.messenger.databinding.ActivitySignInBinding
import ge.rmenagharishvili.messenger.hideLoadingProgressBar
import ge.rmenagharishvili.messenger.mainpage.MainPageActivity
import ge.rmenagharishvili.messenger.showLoadingProgressBar
import ge.rmenagharishvili.messenger.signup.SignUpActivity
import ge.rmenagharishvili.messenger.validFields

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: ViewModel
    private lateinit var handler: Handler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ViewModel::class.java]

        handler = Handler(Looper.getMainLooper())

        // start sign up activity if the user clicks the sign up button
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignIn.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            val pass = binding.etPassword.text.toString()
            if (validFields(this, nickname, pass, null)) {
                handler.post { showLoadingProgressBar(this) }
                viewModel.login(nickname, pass) {it ->
                    handler.post { hideLoadingProgressBar() }
                    if(it) {
                        val intent = Intent(this@SignInActivity, MainPageActivity::class.java)
                        startActivity(intent)
                        this.finish()
                    }
                }
            }
        }
    }
}