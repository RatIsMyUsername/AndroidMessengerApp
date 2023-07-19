package ge.rmenagharishvili.messenger.global_users

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ge.rmenagharishvili.messenger.databinding.GlobalUsersBinding
import ge.rmenagharishvili.messenger.mainpage.MainPageActivity
import kotlinx.coroutines.launch
import ge.rmenagharishvili.messenger.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlin.coroutines.CoroutineContext


class GlobalUsersActivity : AppCompatActivity(), CoroutineScope,GlobalUsersListener{
    private lateinit var binding: GlobalUsersBinding
    public lateinit var viewModel: ViewModel
    private var adapter: Adapter = Adapter(this, mutableListOf<User>(),this)


    companion object{
        const val DEBOUNCE: Long = 300
        const val MIN_LENGTH: Int = 3
    }
    override fun onClickListener(user: User) {
        println("TODO, start chatting with " + user.nickname)
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
            val intent = Intent(this@GlobalUsersActivity, MainPageActivity::class.java)
            startActivity(intent)
            this.finish()
        }


        fetchUsers("")

        binding.searchField.addTextChangedListener(object: TextWatcher{
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
                launch{
                    delay(DEBOUNCE)
                    if(filter == latestVersion && (filter.length >= MIN_LENGTH || filter == "")){
                        fetchUsers(filter)
                    }
                }
            }

        })
    }

    private fun fetchUsers(filter: String){
        viewModel.getUsers(filter){ param: MutableList<User> ->
            adapter.users = param
            adapter.notifyDataSetChanged()
        }
    }

}