package ge.rmenagharishvili.messenger.signup

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import ge.rmenagharishvili.messenger.R
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


    private fun postRegistration(nickname: String, occupation: String, callback: (Unit) -> Unit) {
        dbRoot.child(CHILD_NAME_USERS).child(authService.currentUser!!.uid)
            .setValue(User(nickname = nickname, occupation = occupation)).addOnCompleteListener {
            uploadDefaultImage(authService.currentUser!!.uid)
            fastToast(context, "user successfully registered")
            callback(Unit)
        }.addOnFailureListener {
            fastToast(context, "server error 2"); Log.e(
            "server error 2",
            it.toString()
        )
        }
    }

    private fun uploadDefaultImage(uid: String) {
        val imageUri = Uri.parse("android.resource://${context.packageName}/${R.drawable.avatar_image_placeholder}")
        val storageReference = FirebaseStorage.getInstance().getReference("Users/$uid")
        storageReference.putFile(imageUri)
    }

    fun registerNew(nickname: String, pass: String, occupation: String, callback: (Unit) -> Unit) {
        checkIfUserExists(getMail(nickname)) { exists ->
            if (exists) {
                fastToast(context, "user with given nickname already exists")
            } else {
                authService.createUserWithEmailAndPassword(getMail(nickname), pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            postRegistration(nickname, occupation, callback)
                        } else {
                            fastToast(context, "registration failed")
                        }
                    }.addOnFailureListener {
                    fastToast(
                        context,
                        "server error"
                    ); Log.e("server error", it.toString())
                }
            }
        }
    }
}