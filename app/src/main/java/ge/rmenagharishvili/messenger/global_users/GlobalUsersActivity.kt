package ge.rmenagharishvili.messenger.global_users

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ge.rmenagharishvili.messenger.databinding.GlobalUsersBinding
import ge.rmenagharishvili.messenger.mainpage.MainPageActivity
import ge.rmenagharishvili.messenger.global_users.ViewModel
import ge.rmenagharishvili.messenger.user.User

class GlobalUsersActivity : AppCompatActivity(){
    private lateinit var binding: GlobalUsersBinding
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()

        binding = GlobalUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ViewModel::class.java]

        binding.goBack.setOnClickListener {
            val intent = Intent(this@GlobalUsersActivity, MainPageActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        viewModel.getUsers("rati"){ param: MutableList<User> -> println(param)}
    }
}