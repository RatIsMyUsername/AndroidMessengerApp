package ge.rmenagharishvili.messenger.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ge.rmenagharishvili.messenger.databinding.IncomingMessageBinding
import ge.rmenagharishvili.messenger.databinding.OutgoingMessageBinding

class MessageAdapter(
    val context: Context,
    val messageList: ArrayList<Message>,
    val viewModel: ViewModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == ITEM_RECEIVE) {
            // inflate they is stuff
            val binding = IncomingMessageBinding.inflate(layoutInflater, parent, false)
            IncomingViewHolder(binding)
        } else {
            // inflate i am stuff
            val binding = OutgoingMessageBinding.inflate(layoutInflater, parent, false)
            OutgoingViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return if (viewModel.getCurrentUserId().equals(message.senderId)) {
            ITEM_SEND
        } else {
            ITEM_RECEIVE
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]
        if (holder.javaClass == OutgoingViewHolder::class.java) {
            // i am stuff
            holder as OutgoingViewHolder
            holder.sentMessage.text = message.messageText
        } else {
            // they is stuff
            holder as IncomingViewHolder
            holder.receivedMessage.text = message.messageText
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class OutgoingViewHolder(val binding: OutgoingMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val sentMessage = binding.tvMessage
    }

    class IncomingViewHolder(val binding: IncomingMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val receivedMessage = binding.tvMessage
    }

    companion object {
        private const val ITEM_SEND = 2
        private const val ITEM_RECEIVE = 1
    }

}