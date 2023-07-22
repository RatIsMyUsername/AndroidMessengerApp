package ge.rmenagharishvili.messenger.mainpage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ge.rmenagharishvili.messenger.R
import ge.rmenagharishvili.messenger.databinding.ActivityMainPageBinding

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
    }
}