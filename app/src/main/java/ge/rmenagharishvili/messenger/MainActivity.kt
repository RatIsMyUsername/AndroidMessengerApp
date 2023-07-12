package ge.rmenagharishvili.messenger

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ge.rmenagharishvili.messenger.signin.SignInActivity
import ge.rmenagharishvili.messenger.signup.SignUpActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: check if there is an user signed in
        if (true) {
            // launch sign in(PROFILE) activity if the user is signed in
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
        } else {
            // launch sign up activity if the user is not signed in
            val intent = Intent(this@MainActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        setContentView(R.layout.activity_main)
    }
}