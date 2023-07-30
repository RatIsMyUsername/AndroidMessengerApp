package ge.rmenagharishvili.messenger.home_users

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import ge.rmenagharishvili.messenger.user.Friend

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: Repository

    init {
        repo = Repository(application.applicationContext)
    }

    fun getUsers(searchWith: String, callback: (MutableList<Friend>) -> Unit) {
        repo.getFriendIds(){
            val filteredList = it.filter { friend ->
                // Filter based on the prefix of the friend.nickname.
                friend.nickname?.startsWith(searchWith, ignoreCase = true) == true
            }

            // Sort the filtered list by last_message_time in ascending order.
            val sortedList = filteredList.sortedByDescending { friend ->
                friend.last_message_time
            }

            // Call the callback function with the sorted list.
            callback(sortedList.toMutableList())
        }
    }

    fun getPfpUrlFor(userId: String, callback: (Uri?) -> Unit){
        repo.getPfpUrlFor(userId,callback)
    }
}