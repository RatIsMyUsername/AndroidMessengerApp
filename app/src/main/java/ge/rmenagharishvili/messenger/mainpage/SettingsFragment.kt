package ge.rmenagharishvili.messenger.mainpage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import ge.rmenagharishvili.messenger.*
import ge.rmenagharishvili.messenger.databinding.FragmentSettingsBinding
import ge.rmenagharishvili.messenger.signin.SignInActivity
import ge.rmenagharishvili.messenger.signup.Repository.Companion.CHILD_NAME_USERS
import ge.rmenagharishvili.messenger.user.User

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var pictureResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var handler: Handler

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child(CHILD_NAME_USERS)

        pictureResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    // There are no request codes
                    onPictureSelected(result)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        handler = Handler(Looper.getMainLooper())

        // get current user information and display it on this page
        val currentUser = auth.currentUser
        if (currentUser != null) {
            handler.post { showLoadingProgressBar(this.requireContext()) }
            val userReference = databaseReference.child(currentUser.uid)
            userReference.get().addOnSuccessListener { dataSnapshot ->
                var listenerDone = false
                var picLoaded = false
                if (dataSnapshot.exists()) {
                    // User data found
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        val nickname = user.nickname
                        val occupation = user.occupation

                        binding.etNickname.setText(nickname)
                        binding.etWhatIDo.setText(occupation)

                        storageReference =
                            FirebaseStorage.getInstance().reference.child("Users/${currentUser.uid}")
                        storageReference.downloadUrl.addOnSuccessListener { uri ->
                            Picasso.get().load(uri).into(binding.ivProfilePicture)
                            picLoaded = true
                            if (listenerDone) {
                                handler.post { hideLoadingProgressBar() }
                            }
                        }.addOnFailureListener {
                            fastToast(this.requireContext(), "Unable to load profile picture")
                            picLoaded = true
                            if (listenerDone) {
                                handler.post { hideLoadingProgressBar() }
                            }
                        }
                    }
                } else {
                    fastToast(this.requireContext(), "User data not found")
                    handler.post { hideLoadingProgressBar() }
                }
                listenerDone = true
                if (picLoaded) {
                    handler.post { hideLoadingProgressBar() }
                }
            }.addOnFailureListener {
                fastToast(this.requireContext(), "Error displaying user data")
                handler.post { hideLoadingProgressBar() }
            }
        }

        // set on click listeners for user update information
        binding.ivProfilePicture.setOnClickListener { selectPicture() }

        binding.btnUpdate.setOnClickListener { update() }

        binding.btnSignOut.setOnClickListener { signOut() }
    }

    private fun selectPicture() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pictureResultLauncher.launch(intent)
    }

    private fun onPictureSelected(result: ActivityResult) {
        val data: Intent? = result.data
        if (data == null) {
            fastToast(this.requireContext(), "Image data is null")
            return
        }
        val selectedImage = data.data
        imageUri = selectedImage
        binding.ivProfilePicture.setImageURI(imageUri)
    }

    private fun update() {
        handler.post { showLoadingProgressBar(this.requireContext()) }
        // UID of the currently signed in user
        val uid = auth.currentUser?.uid

        // get the information from the fields
        val nickname = binding.etNickname.text.toString()
        val occupation = binding.etWhatIDo.text.toString()

        if (!validFields(this.requireContext(), nickname, "passable_password", occupation)) {
            handler.post { hideLoadingProgressBar() }
            return
        }

        val user = User(nickname, occupation)
        // update user information based on what is provided on this page
        if (uid != null) {
            databaseReference.child(uid).setValue(user).addOnCompleteListener {
                if (it.isSuccessful) {
                    updateEmail(user.nickname!!)
                    updatePicture()
                } else {
                    fastToast(this.requireContext(), "Failed to update profile")
                    handler.post { hideLoadingProgressBar() }
                }
            }
        }
    }

    private fun updateEmail(nickname: String) {
        val user = auth.currentUser
        user?.updateEmail(getMail(nickname))
    }

    private fun updatePicture() {
        if (imageUri != null) {
            storageReference =
                FirebaseStorage.getInstance().getReference("Users/${auth.currentUser?.uid}")
            storageReference.putFile(imageUri!!).addOnSuccessListener {
                fastToast(this.requireContext(), "Profile successfully updated")
                handler.post { hideLoadingProgressBar() }
            }.addOnFailureListener {
                fastToast(this.requireContext(), "Failed to upload profile picture")
                handler.post { hideLoadingProgressBar() }
            }
        } else {
            fastToast(this.requireContext(), "Profile successfully updated")
            handler.post { hideLoadingProgressBar() }
        }
    }

    private fun signOut() {
        // sign out of the application
        auth.signOut()
        // go back to the sign in page and finish this activity
        val intent = Intent(this.requireContext(), SignInActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SettingsFragment.
         */
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}