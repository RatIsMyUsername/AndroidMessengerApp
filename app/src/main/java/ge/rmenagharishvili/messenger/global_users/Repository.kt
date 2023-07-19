package ge.rmenagharishvili.messenger.global_users

import android.content.Context
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ge.rmenagharishvili.messenger.fastToast
import ge.rmenagharishvili.messenger.user.User


class Repository(private val context: Context) {
    private var dbRoot: DatabaseReference = FirebaseDatabase.getInstance().reference

    companion object {
        const val CHILD_NAME_USERS = "USERS"
        const val STOP_CODE = "\uf8ff"
    }

    fun getUsers(searchWith: String, callback: (MutableList<User>) -> Unit) {
        dbRoot.child(CHILD_NAME_USERS).get().addOnSuccessListener {
            val res = mutableListOf<User>()
            for(entry in it.children){
                res.add(entry.getValue(User::class.java) as User)
                println("loaded")
            }
            callback(res)
        }.addOnFailureListener {
            fastToast(context,"could not retrieve user list")
            Log.e("server error when retrieving users", it.toString())
        }
    }
}