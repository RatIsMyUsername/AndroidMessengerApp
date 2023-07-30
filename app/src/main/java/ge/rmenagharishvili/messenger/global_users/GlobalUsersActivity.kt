package ge.rmenagharishvili.messenger.global_users

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ge.rmenagharishvili.messenger.DEBOUNCE
import ge.rmenagharishvili.messenger.MIN_LENGTH
import ge.rmenagharishvili.messenger.chat.ChatActivity
import ge.rmenagharishvili.messenger.databinding.GlobalUsersBinding
import ge.rmenagharishvili.messenger.fastToast
import ge.rmenagharishvili.messenger.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class GlobalUsersActivity : AppCompatActivity(), CoroutineScope, GlobalUsersListener {
    private lateinit var binding: GlobalUsersBinding
    public lateinit var viewModel: ViewModel
    private var adapter: Adapter = Adapter(this, mutableListOf<User>(), this)

    override fun onClickListener(user: User) {
        val intent = Intent(this@GlobalUsersActivity, ChatActivity::class.java)
        intent.putExtra("nickname", user.nickname)
        intent.putExtra("uid", user.uid)
        intent.putExtra("occupation", user.occupation)
        startActivity(intent)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()

        binding = GlobalUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.usersRc.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ViewModel::class.java]


        binding.goBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        fetchUsers("")

        binding.searchField.addTextChangedListener(object : TextWatcher {
            private var latestVersion: String = ""

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                //do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var filter = p0.toString()
                filter = filter.trim()
                latestVersion = filter
                launch {
                    delay(DEBOUNCE)
                    if (filter == latestVersion && (filter.length >= MIN_LENGTH || filter == "")) {
                        fetchUsers(filter)
                    }
                }
            }

        })
    }

    private fun fetchUsers(filter: String) {
        viewModel.getUsers(filter) { param: MutableList<User> ->
            adapter.users = param
            adapter.notifyDataSetChanged()
            if (param.size == 0) {
                fastToast(application.applicationContext, "No user found with given filter")
            }
        }
    }

}