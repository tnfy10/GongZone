package kr.co.wanted.gongzone.view

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarTransparent()

        val nearMeFragment = NearMeFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, nearMeFragment)
            .commit()

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nearMe -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, nearMeFragment)
                        .commit()
                }
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

    /**
     * 상태바는 투명하게 내비게이션 바는 투명이 적용되지 않게 하는 메서드
     */
    private fun setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT in 26..29) {
            window.statusBarColor = Color.TRANSPARENT
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        } else if (Build.VERSION.SDK_INT >= 30) {
            window.statusBarColor = Color.TRANSPARENT
            // Making status bar overlaps with the activity
            WindowCompat.setDecorFitsSystemWindows(window, false)

            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->

                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

                // Apply the insets as a margin to the view. Here the system is setting
                // only the bottom, left, and right dimensions, but apply whichever insets are
                // appropriate to your layout. You can also update the view padding
                // if that's more appropriate.

                view.layoutParams =  (view.layoutParams as FrameLayout.LayoutParams).apply {
                    leftMargin = insets.left
                    bottomMargin = insets.bottom
                    rightMargin = insets.right
                }

                // Return CONSUMED if you don't want want the window insets to keep being
                // passed down to descendant views.
                WindowInsetsCompat.CONSUMED
            }
        }
    }
}