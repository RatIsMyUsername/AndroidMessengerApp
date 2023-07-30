package ge.rmenagharishvili.messenger.chat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ge.rmenagharishvili.messenger.*
import ge.rmenagharishvili.messenger.databinding.ActivityChatBinding
import ge.rmenagharishvili.messenger.user.User

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var viewModel: ViewModel
    private lateinit var chatRV: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter

    private lateinit var handler: Handler


    private lateinit var messageList: ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ViewModel::class.java]
        handler = Handler(Looper.getMainLooper())

        binding.toolbar.setNavigationIcon(R.drawable.baseline_chevron_left_36)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        val receiverNickname = intent.getStringExtra("nickname")
        val receiverId = intent.getStringExtra("uid")
        val receiverOccupation = intent.getStringExtra("occupation")
        val receiver = User(nickname = receiverNickname, occupation = receiverOccupation, uid = receiverId)

        viewModel.loadPicture(receiverId!!, binding.ivUserPic)

        val senderId = viewModel.getCurrentUserId()
        var sender = User(uid=senderId)
        viewModel.fillUser(senderId!!){
            if(it!=null){
                sender = it
            }
        }

        binding.tvNickname.text = receiverNickname
        binding.tvOccupation.text = receiverOccupation

        chatRV = binding.rvChat
        messageBox = binding.etMessage
        sendButton = binding.ivCircle

        messageList = ArrayList()

        messageAdapter = MessageAdapter(this, messageList, viewModel)

        chatRV.adapter = messageAdapter

        // add data to RecyclerView
        handler.post { showLoadingProgressBar(this) }
        viewModel.updateMessageList(senderId!!, receiverId!!, messageList, {
            handler.post {
                hideLoadingProgressBar()
                messageAdapter.notifyDataSetChanged()
                chatRV.scrollToPosition(messageAdapter.itemCount - 1);
                binding.appBar.setExpanded(false,true)
            }
        }, {
            handler.post {
                hideLoadingProgressBar()
                fastToast(this, "Failed to load messages")
            }
        })

        // add the message to the database
        sendButton.setOnClickListener {
            val messageText = binding.etMessage.text.toString()
            val message = Message(
                messageText,
                senderId,
                timeInTimezoneMillis()
            )

            if (viewModel.sendMessage(
                    sender,
                    receiver, message
                )
            ) {
                messageBox.setText("")
            }

        }
    }
}
