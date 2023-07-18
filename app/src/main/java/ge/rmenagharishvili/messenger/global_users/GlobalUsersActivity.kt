package ge.rmenagharishvili.messenger.global_users

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ge.rmenagharishvili.messenger.databinding.GlobalUsersBinding

class GlobalUsersActivity : AppCompatActivity(){
    private lateinit var binding: GlobalUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()

        binding = GlobalUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}