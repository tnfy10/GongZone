package kr.co.wanted.gongzone.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.fragment.app.FragmentTransaction
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, NearMeFragment.newInstance())
            .commit()

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nearMe -> supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, NearMeFragment.newInstance())
                    .commit()
                R.id.search -> supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance())
                    .commit()
                R.id.useStatus -> supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, UseStatusFragment.newInstance())
                    .commit()
                R.id.my -> supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, MyFragment.newInstance())
                    .commit()
            }
            true
        }
    }
}