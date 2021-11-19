package kr.co.wanted.gongzone.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}