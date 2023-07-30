package ge.rmenagharishvili.messenger.mainpage

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ge.rmenagharishvili.messenger.*
import ge.rmenagharishvili.messenger.chat.ChatActivity
import ge.rmenagharishvili.messenger.databinding.FragmentHomeBinding
import ge.rmenagharishvili.messenger.home_users.ViewModel
import ge.rmenagharishvili.messenger.user.Friend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), CoroutineScope, HomeUsersListener {
    private lateinit var binding: FragmentHomeBinding
    public lateinit var viewModel: ViewModel
    private var adapter: Adapter = Adapter(this, mutableListOf<Friend>(), this)

    override fun onClickListener(user: Friend) {
        val intent = Intent(requireActivity(), ChatActivity::class.java)
        intent.putExtra("nickname", user.nickname)
        intent.putExtra("uid", user.uid)
        intent.putExtra("occupation", user.occupation)
        startActivity(intent)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        binding.rvFriends.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ViewModel::class.java]

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
                println("CHANGED")
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

        val mainActivity = requireActivity() as MainPageActivity

        binding.rvFriends.addOnScrollListener( object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mainActivity.navbarVisible()
                }else{
                    mainActivity.navbarInvisible()
                }
            }
        })
    }

    private fun fetchUsers(filter: String) {
        viewModel.getUsers(filter) { param: MutableList<Friend> ->
            adapter.users = param
            adapter.notifyDataSetChanged()
            if (param.size == 0) {
                fastToast(requireActivity().application.applicationContext, "No user found with given filter")
            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}