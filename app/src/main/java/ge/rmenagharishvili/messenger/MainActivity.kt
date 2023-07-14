package ge.rmenagharishvili.messenger

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ge.rmenagharishvili.messenger.mainpage.MainPageActivity
import ge.rmenagharishvili.messenger.signin.SignInActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()

        // TODO: check if there is an user signed in
        if (true) {
            // launch main page activity if the user is signed in
            val intent = Intent(this@MainActivity, MainPageActivity::class.java)
            startActivity(intent)
            this.finish()
        } else {
            // launch sign in activity if the user is not signed in
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        setContentView(R.layout.activity_main)
    }
}