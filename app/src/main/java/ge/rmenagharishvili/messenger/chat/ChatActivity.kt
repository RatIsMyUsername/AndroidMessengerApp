package ge.rmenagharishvili.messenger.chat

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ge.rmenagharishvili.messenger.R
import ge.rmenagharishvili.messenger.databinding.ActivityChatBinding
import ge.rmenagharishvili.messenger.timeInTimezoneMillis

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var viewModel: ViewModel
    private lateinit var chatRV: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter

    private lateinit var messageList: ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ViewModel::class.java]

        binding.toolbar.setNavigationIcon(R.drawable.baseline_chevron_left_36)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val receiverNickname = intent.getStringExtra("nickname")
        val receiverId = intent.getStringExtra("uid")

        val senderId = viewModel.getCurrentUserId()

        binding.tvNickname.text = receiverNickname

        chatRV = binding.rvChat
        messageBox = binding.etMessage
        sendButton = binding.ivCircle

        messageList = ArrayList()

        messageAdapter = MessageAdapter(this, messageList, viewModel)

        // add data to RecyclerView


        // add the message to the database
        sendButton.setOnClickListener {
            val messageText = binding.etMessage.text.toString()
            val message = Message(
                messageText,
                senderId,
                timeInTimezoneMillis()
            )

            senderId?.let { it1 ->
                receiverId?.let { it2 ->
                    if (viewModel.sendMessage(
                            it1,
                            it2, message
                        )
                    ) {
                        messageBox.setText("")
                    }
                }
            }

        }
    }
}
