package kr.co.wanted.gongzone.view.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.FragmentStoreInfoBinding
import kr.co.wanted.gongzone.utils.PreferenceManager
import kr.co.wanted.gongzone.viewmodel.StoreViewModel

class StoreInfoFragment : Fragment() {

    private lateinit var binding: FragmentStoreInfoBinding
    private lateinit var viewModel: StoreViewModel
    private lateinit var storeActivity: StoreActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeActivity = activity as StoreActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoreInfoBinding.inflate(inflater, container, false)

        val spaceGuideFragment = SpaceGuideFragment.newInstance()
        val facilitiesAndRentalFragment = FacilitiesAndRentalFragment.newInstance()
        val reviewFragment = ReviewFragment.newInstance()
        val locationFragment = LocationFragment.newInstance()

        binding.tabs.addTab(binding.tabs.newTab().setText("공간 안내"))
        binding.tabs.addTab(binding.tabs.newTab().setText("시설·대여"))
        binding.tabs.addTab(binding.tabs.newTab().setText("후기"))
        binding.tabs.addTab(binding.tabs.newTab().setText("위치"))

        changeTabView(spaceGuideFragment)

        binding.backBtn.setOnClickListener {
            storeActivity.finish()
        }

        binding.tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> changeTabView(spaceGuideFragment)
                    1 -> changeTabView(facilitiesAndRentalFragment)
                    2 -> changeTabView(reviewFragment)
                    3 -> changeTabView(locationFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })

        binding.enterRoomBtn.setOnClickListener {
            if (checkSigned()) {
                storeActivity.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.container, storeActivity.enterRoomFragment)
                    addToBackStack(null)
                    commit()
                }
            } else {
                Toast.makeText(context, "로그인 후 이용가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(storeActivity)[StoreViewModel::class.java]
        viewModel.getSpaceLiveData().observe(viewLifecycleOwner, { spaceItem ->
            binding.storeIntroTxt.text = spaceItem.introduce
            "${spaceItem.spaceName}·${spaceItem.spaceType}".also { binding.storeNameAndKindTxt.text = it }
            Glide.with(storeActivity).load(spaceItem.imagePath).into(binding.storeImg)
        })
    }

    private fun changeTabView(selected: Fragment) {
        childFragmentManager.beginTransaction().apply {
            replace(binding.tabView.id, selected)
            commit()
        }
    }

    /**
     * 로그인 여부 확인
     */
    private fun checkSigned()
            = PreferenceManager.getBoolean(storeActivity, "isSigned")

    companion object {
        fun newInstance(): StoreInfoFragment {
            return StoreInfoFragment()
        }
    }
}