package ge.rmenagharishvili.messenger

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ge.rmenagharishvili.messenger.chat.ChatActivity
import ge.rmenagharishvili.messenger.mainpage.MainPageActivity
import ge.rmenagharishvili.messenger.signin.SignInActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this@MainActivity, ChatActivity::class.java)
        intent.putExtra("nickname", "Obama")
        intent.putExtra("uid", "pcTtN2KjPMNAPiMVboj5xXBHFJr1")
        startActivity(intent)
        return
        // check if there is an user signed in
        if (FirebaseAuth.getInstance().currentUser != null) {
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