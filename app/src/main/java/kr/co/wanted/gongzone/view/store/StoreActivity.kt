package kr.co.wanted.gongzone.view.store

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.ActivityStoreBinding

class StoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, StoreInfoFragment.newInstance())
            .commit()
    }
}