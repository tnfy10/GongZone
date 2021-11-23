package kr.co.wanted.gongzone.view.store

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.ActivityStoreBinding

class StoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreBinding
    lateinit var storeInfoFragment: StoreInfoFragment
    lateinit var enterRoomFragment: EnterRoomFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

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