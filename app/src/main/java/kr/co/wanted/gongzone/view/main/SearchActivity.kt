package kr.co.wanted.gongzone.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.wanted.gongzone.adapter.SpaceAdapter
import kr.co.wanted.gongzone.databinding.ActivitySearchBinding
import kr.co.wanted.gongzone.model.space.Space
import kr.co.wanted.gongzone.service.SpaceService
import kr.co.wanted.gongzone.view.store.StoreActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        binding.searchEdt.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                IME_ACTION_SEARCH -> {
                    val searchTxt = binding.searchEdt.text.toString()
                    val intent = Intent(applicationContext, SearchResultActivity::class.java)
                    intent.putExtra("searchTxt", searchTxt)
                    startActivity(intent)
                }
            }
            return@setOnEditorActionListener true
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