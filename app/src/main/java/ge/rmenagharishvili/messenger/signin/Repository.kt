package ge.rmenagharishvili.messenger.signin

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ge.rmenagharishvili.messenger.fastToast
import ge.rmenagharishvili.messenger.getMail
import ge.rmenagharishvili.messenger.user.User


class Repository(private val context: Context) {
    private var authService: FirebaseAuth = FirebaseAuth.getInstance()
    private var dbRoot: DatabaseReference = FirebaseDatabase.getInstance().reference

    companion object {
        const val CHILD_NAME_USERS = "USERS"
    }

    fun login(nickname: String, pass: String){
        authService.signInWithEmailAndPassword(getMail(nickname),pass).addOnSuccessListener { fastToast(context,"login sucessful") }.addOnFailureListener { fastToast(context, "login failed") }
    }
}