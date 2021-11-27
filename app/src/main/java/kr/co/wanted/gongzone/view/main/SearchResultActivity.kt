package kr.co.wanted.gongzone.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.adapter.SearchSpaceAdapter
import kr.co.wanted.gongzone.adapter.SpaceAdapter
import kr.co.wanted.gongzone.databinding.ActivitySearchResultBinding
import kr.co.wanted.gongzone.model.space.Space
import kr.co.wanted.gongzone.service.SpaceService
import kr.co.wanted.gongzone.view.store.StoreActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchTxt = intent.getStringExtra("searchTxt")

        if (searchTxt != null) {
            binding.searchTxt.text = searchTxt
            searchSpace(searchTxt)
        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun searchSpace(searchTxt: String) {
        SpaceService.create().searchSpace(searchTxt).enqueue(object: Callback<Space> {
            override fun onResponse(call: Call<Space>, response: Response<Space>) {
                if (response.isSuccessful) {
                    val resultList = response.body()
                    if (resultList != null) {
                        val adapter = SearchSpaceAdapter()
                        adapter.listData = resultList
                        adapter.setOnItemClickListener(object: SearchSpaceAdapter.OnItemClickListener{
                            override fun onItemClick(v: View, pos: Int) {
                                val intent = Intent(applicationContext, StoreActivity::class.java)
                                intent.putExtra("spaceNum", resultList[pos].spaceNum)
                                startActivity(intent)
                            }
                        })
                        binding.searchList.adapter = adapter
                        binding.searchList.layoutManager = LinearLayoutManager(applicationContext)
                    }
                }
            }

            override fun onFailure(call: Call<Space>, t: Throwable) {
                Toast.makeText(applicationContext, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                Log.d("검색", "통신 에러: ${t.message}")
            }
        })
    }
}