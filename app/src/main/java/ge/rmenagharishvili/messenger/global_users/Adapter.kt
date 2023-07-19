package ge.rmenagharishvili.messenger.global_users

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ge.rmenagharishvili.messenger.R
import ge.rmenagharishvili.messenger.user.User

interface GlobalUsersListener{
    fun onClickListener(user: User)
}
class HolderGlobal(itemView: View): RecyclerView.ViewHolder(itemView) {
    var avatar: ImageView = itemView.findViewById(R.id.single_pfp)
    var nickname: TextView = itemView.findViewById(R.id.single_nickname)
    var occupation: TextView = itemView.findViewById(R.id.single_job)
}

class Adapter(var globalUsersActivity: GlobalUsersActivity, var users: MutableList<User>, var response: GlobalUsersListener): RecyclerView.Adapter<HolderGlobal>() {

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderGlobal {
        return HolderGlobal(LayoutInflater.from(parent.context).inflate(R.layout.single_user,parent,false))
    }

    override fun onBindViewHolder(holder: HolderGlobal, position: Int) {
        val user: User = users[position]
        holder.nickname.text = user.nickname
        holder.occupation.text = user.occupation
        holder.itemView.setOnClickListener{response.onClickListener(user)}
        //load pfp lazy
        holder.avatar.setImageResource(R.drawable.avatar_image_placeholder)
        user.uid?.let { globalUsersActivity.viewModel.getPfpUrlFor(it){ param: Uri ->
            Picasso.get().load(param).into(holder.avatar)
        } }
    }

}


