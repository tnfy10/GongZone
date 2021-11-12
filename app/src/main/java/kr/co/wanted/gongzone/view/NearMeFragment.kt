package kr.co.wanted.gongzone.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.naver.maps.map.LocationSource
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.BottomSheetMainBinding
import kr.co.wanted.gongzone.databinding.FragmentNearMeBinding
import kr.co.wanted.gongzone.databinding.NavMenuMainBinding

class NearMeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentNearMeBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var mainNavMenu: NavMenuMainBinding
    private lateinit var mainBottomSheet: BottomSheetMainBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: LocationSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearMeBinding.inflate(inflater, container, false)

        mainActivity = (activity as MainActivity)
        mainNavMenu = mainActivity.binding.includedNavView
        mainBottomSheet = binding.includedMainBottomSheet

        binding.hamburgerMenu.setOnClickListener {
            mainActivity.binding.drawerLayout.openDrawer(mainNavMenu.navigationView)
        }

        mainNavMenu.useHistoryBtn.setOnClickListener {
            navBtnTest("이용내역")
        }

        mainNavMenu.ticketPurchaseBtn.setOnClickListener {
            navBtnTest("이용권 구매")
        }

        mainNavMenu.ticketPurchaseHistoryBtn.setOnClickListener {
            navBtnTest("이용권 구매 내역")
        }

        mainNavMenu.favoritesBtn.setOnClickListener {
            navBtnTest("관심 목록")
        }

        mainNavMenu.couponBtn.setOnClickListener {
            navBtnTest("쿠폰")
        }

        mainNavMenu.noticeBtn.setOnClickListener {
            navBtnTest("공지사항")
        }

        mainNavMenu.customerServiceBtn.setOnClickListener {
            navBtnTest("고객센터")
        }

        mainNavMenu.settingBtn.setOnClickListener {
            navBtnTest("설정")
        }

        val behavior = BottomSheetBehavior.from<View>(mainBottomSheet.bottomSheet)

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // TODO: 상태가 변할 때 작동할 내용
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // TODO: 슬라이드 중일 때 작동할 내용
            }
        })

        return binding.root
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        naverMap.maxZoom = 18.0
    }

    /**
     * 네비게이션 드로어 버튼 테스트용
     */
    private fun navBtnTest(content: String) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
        mainActivity.binding.drawerLayout.closeDrawers()
    }

    companion object {
        fun newInstance() : NearMeFragment {
            return NearMeFragment()
        }
    }
}