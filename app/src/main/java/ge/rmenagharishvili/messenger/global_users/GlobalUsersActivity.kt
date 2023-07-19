package ge.rmenagharishvili.messenger.global_users

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ge.rmenagharishvili.messenger.databinding.GlobalUsersBinding
import ge.rmenagharishvili.messenger.mainpage.MainPageActivity

class GlobalUsersActivity : AppCompatActivity(){
    private lateinit var binding: GlobalUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()

        binding = GlobalUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goBack.setOnClickListener {
            val intent = Intent(this@GlobalUsersActivity, MainPageActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }
}