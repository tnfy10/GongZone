package kr.co.wanted.gongzone.view.store

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.ActivityStoreBinding
import kr.co.wanted.gongzone.viewmodel.StoreViewModel

class StoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreBinding
    private lateinit var viewModel: StoreViewModel
    lateinit var storeInfoFragment: StoreInfoFragment
    lateinit var enterRoomFragment: EnterRoomFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spaceNum = intent.getStringExtra("spaceNum")

        if (spaceNum != null) {
            viewModel = ViewModelProvider(this).get(StoreViewModel::class.java)
            viewModel.setSpaceLiveData(spaceNum)
            viewModel.setSeatLiveData(spaceNum)
        } else {
            Log.d("StoreActivity", "spaceNum값이 없음.")
            finish()
        }

        storeInfoFragment = StoreInfoFragment.newInstance()
        enterRoomFragment = EnterRoomFragment.newInstance()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, storeInfoFragment)
            commit()
        }
    }

    override fun onBackPressed() {
        if (enterRoomFragment.isVisible) {
            if (enterRoomFragment.childFragmentManager.backStackEntryCount>=1) {
                enterRoomFragment.childFragmentManager.popBackStackImmediate()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }
}