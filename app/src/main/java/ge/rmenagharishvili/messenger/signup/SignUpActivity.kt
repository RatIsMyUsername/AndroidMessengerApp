package ge.rmenagharishvili.messenger.signup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ge.rmenagharishvili.messenger.databinding.ActivitySignUpBinding
import ge.rmenagharishvili.messenger.hideLoadingProgressBar
import ge.rmenagharishvili.messenger.mainpage.MainPageActivity
import ge.rmenagharishvili.messenger.showLoadingProgressBar
import ge.rmenagharishvili.messenger.validFields

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: ViewModel
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ViewModel::class.java]

        handler = Handler(Looper.getMainLooper())

        binding.btnSignUp.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            val pass = binding.etPassword.text.toString()
            val occupation = binding.etWhatIDo.text.toString()
            if (validFields(this, nickname, pass, occupation)) {
                handler.post { showLoadingProgressBar(this) }
                viewModel.registerNew(nickname, pass, occupation) {
                    handler.post { hideLoadingProgressBar() }
                    val intent = Intent(this@SignUpActivity, MainPageActivity::class.java)
                    startActivity(intent)
                    this.finish()
                }
            }
        }
    }
}
