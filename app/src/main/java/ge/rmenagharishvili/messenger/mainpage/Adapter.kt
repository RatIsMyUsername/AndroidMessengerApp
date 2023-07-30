package ge.rmenagharishvili.messenger.mainpage

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ge.rmenagharishvili.messenger.R
import ge.rmenagharishvili.messenger.hourAndMinute2FromMillis
import ge.rmenagharishvili.messenger.hourAndMinuteFromMillis
import ge.rmenagharishvili.messenger.user.Friend

interface HomeUsersListener{
    fun onClickListener(user: Friend)
}
class HolderHome(itemView: View): RecyclerView.ViewHolder(itemView) {
    var avatar: ImageView = itemView.findViewById(R.id.home_pfp)
    var nickname: TextView = itemView.findViewById(R.id.home_nickname)
    var message: TextView = itemView.findViewById(R.id.home_last_message)
    var time: TextView = itemView.findViewById(R.id.home_time)
}

class Adapter(var homeUsersActivity: HomeFragment, var users: MutableList<Friend>, var response: HomeUsersListener): RecyclerView.Adapter<HolderHome>() {

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderHome {
        return HolderHome(LayoutInflater.from(parent.context).inflate(R.layout.home_user_single,parent,false))
    }

    override fun onBindViewHolder(holder: HolderHome, position: Int) {
        val user: Friend = users[position]
        holder.nickname.text = user.nickname
        holder.itemView.setOnClickListener{response.onClickListener(user)}
        holder.message.text = user.last_message
        holder.time.text = hourAndMinute2FromMillis(user.last_message_time!!)

        holder.avatar.setImageResource(R.drawable.avatar_image_placeholder)
        user.uid?.let { homeUsersActivity.viewModel.getPfpUrlFor(it){ param: Uri? ->
            if(param!=null){
                Picasso.get().load(param).into(holder.avatar)
            }
            else{
                holder.avatar.setImageResource(R.drawable.avatar_image_placeholder)
            }
        } }
    }

}


