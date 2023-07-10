package ge.rmenagharishvili.messenger.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ge.rmenagharishvili.messenger.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: implement sign up functionality
    }
}