package ge.rmenagharishvili.messenger.mainpage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import ge.rmenagharishvili.messenger.R
import ge.rmenagharishvili.messenger.databinding.FragmentSettingsBinding
import ge.rmenagharishvili.messenger.signin.SignInActivity

// TODO: Rename parameter arguments, choose names that match
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
    private lateinit var pictureResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pictureResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

        // TODO: get current user information and display it on this page

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
        val selectedImage = data!!.data
        binding.ivProfilePicture.setImageURI(selectedImage)
    }

    private fun update() {
        val nickname = binding.etNickname.text
        val whatIDo = binding.etWhatIDo.text

        // TODO: update user information based on what is provided on this page
    }

    private fun signOut() {
        // TODO: sign out of the application

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