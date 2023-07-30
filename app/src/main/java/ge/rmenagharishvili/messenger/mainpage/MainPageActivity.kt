package ge.rmenagharishvili.messenger.mainpage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ge.rmenagharishvili.messenger.R
import ge.rmenagharishvili.messenger.databinding.ActivityMainPageBinding
import ge.rmenagharishvili.messenger.global_users.GlobalUsersActivity

class MainPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pager = binding.viewPager2
        val adapter = MainPageAdapter(supportFragmentManager, lifecycle)
        pager.adapter = adapter
        pager.isUserInputEnabled = false


        binding.bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    pager.currentItem = 0
                    true
                }
                R.id.settings -> {
                    pager.currentItem = 1
                    true
                }
                else -> false
            }
        }

        binding.floatingButton.setOnClickListener {
            val intent = Intent(this@MainPageActivity, GlobalUsersActivity::class.java)
            startActivity(intent)
        }
    }

    fun navbarVisible(){
        binding.coordinator.visibility = View.VISIBLE
    }

    fun navbarInvisible(){
        binding.coordinator.visibility = View.GONE
    }
}