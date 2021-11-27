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
                    searchSpace(searchTxt)
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

    private fun searchSpace(searchTxt: String) {
        SpaceService.create().searchSpace(searchTxt).enqueue(object: Callback<Space> {
            override fun onResponse(call: Call<Space>, response: Response<Space>) {
                if (response.isSuccessful) {
                    val resultList = response.body()
                    if (resultList != null) {
                        val adapter = SpaceAdapter()
                        adapter.listData = resultList
                        adapter.setOnItemClickListener(object: SpaceAdapter.OnItemClickListener{
                            override fun onItemClick(v: View, pos: Int) {
                                val intent = Intent(applicationContext, StoreActivity::class.java)
                                intent.putExtra("spaceNum", resultList[pos].spaceNum)
                                startActivity(intent)
                            }
                        })
                        binding.searchList.adapter = adapter
                        binding.searchList.layoutManager = LinearLayoutManager(applicationContext)
                        binding.searchList.visibility = VISIBLE
                        binding.defaultLayout.visibility = GONE
                    } else {
                        Toast.makeText(applicationContext, "검색 결과가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                        binding.searchList.visibility = GONE
                        binding.defaultLayout.visibility = VISIBLE
                    }
                } else {
                    Toast.makeText(applicationContext, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    binding.searchList.visibility = GONE
                    binding.defaultLayout.visibility = VISIBLE
                }
            }

            override fun onFailure(call: Call<Space>, t: Throwable) {
                Toast.makeText(applicationContext, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                Log.d("검색", "통신 에러: ${t.message}")
                binding.searchList.visibility = GONE
                binding.defaultLayout.visibility = VISIBLE
            }
        })
    }
}