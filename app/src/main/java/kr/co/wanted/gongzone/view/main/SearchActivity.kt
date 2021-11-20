package kr.co.wanted.gongzone.view.main

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import kr.co.wanted.gongzone.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.searchEdt.requestFocus()
            imm.showSoftInput(binding.searchEdt, InputMethodManager.SHOW_FORCED)
        }, 100)
    }

    override fun onPause() {
        super.onPause()
        imm.hideSoftInputFromWindow(binding.searchEdt.windowToken, 0)
    }
}