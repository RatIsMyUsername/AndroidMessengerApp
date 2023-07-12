package ge.rmenagharishvili.messenger.signup

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


    private fun checkIfUserExists(email: String, onComplete: (Boolean) -> Unit) {
        val auth = FirebaseAuth.getInstance()

        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    val userExists = signInMethods != null && signInMethods.isNotEmpty()
                    onComplete(userExists)
                } else {
                    onComplete(false)
                }
            }
    }


    private fun postRegistration(nickname: String, occupation: String){
        dbRoot.child(CHILD_NAME_USERS).child(authService.currentUser!!.uid).setValue(User(nickname=nickname,occupation=occupation)).addOnCompleteListener {
            fastToast(context, "user successfully registered")
        }.addOnFailureListener { fastToast(context, "server error 2"); Log.e("server error 2",it.toString()) }
    }

    fun registerNew(nickname: String, pass: String, occupation: String){
        checkIfUserExists(getMail(nickname)) { exists ->
            if (exists) {
                fastToast(context,"user with given nickname already exists")
            } else {
                authService.createUserWithEmailAndPassword(getMail(nickname),pass).addOnCompleteListener{
                    if(it.isSuccessful){
                        postRegistration(nickname,occupation)
                    }else{
                        fastToast(context,"registration failed")
                    }
                }.addOnFailureListener { fastToast(context,"server error"); Log.e("server error", it.toString()) }
            }
        }
    }
}