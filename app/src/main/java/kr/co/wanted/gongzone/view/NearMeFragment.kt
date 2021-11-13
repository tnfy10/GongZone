package kr.co.wanted.gongzone.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.View.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.BottomSheetMainBinding
import kr.co.wanted.gongzone.databinding.FragmentNearMeBinding
import kr.co.wanted.gongzone.databinding.NavMenuMainBinding

class NearMeFragment : Fragment(), OnMapReadyCallback, Overlay.OnClickListener {

    private lateinit var binding: FragmentNearMeBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var mainNavMenu: NavMenuMainBinding
    private lateinit var mainBottomSheet: BottomSheetMainBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: LocationSource
    private lateinit var getSignInResult: ActivityResultLauncher<Intent>
    lateinit var behavior: BottomSheetBehavior<View>
    lateinit var mapView: FragmentContainerView
    private var isSigned = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = (activity as MainActivity)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearMeBinding.inflate(inflater, container, false)

        mainActivity.setSupportActionBar(binding.toolbar)
        binding.toolbar.setPadding(0, mainActivity.getStatusBarHeight(), 0, 0)

        val fm = mainActivity.supportFragmentManager
        val mapFragment: MapFragment? = MapFragment.newInstance()
        mapFragment?.let { fm.beginTransaction().add(R.id.mapView, it).commit() }
        mapFragment?.getMapAsync(this)

        locationSource = FusedLocationSource(this, PERMISSION_REQUEST_CODE)

        mainNavMenu = mainActivity.binding.includedNavView
        mainBottomSheet = binding.includedMainBottomSheet
        behavior = BottomSheetBehavior.from(mainBottomSheet.bottomSheet)
        mapView = binding.mapView

        getSignInResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                isSigned = it.data?.getBooleanExtra("isSigned", false) == true
                if (isSigned) {
                    val id = it.data?.getStringExtra("id")
                    mainNavMenu.notSignInView.visibility = GONE
                    mainNavMenu.signInView.visibility = VISIBLE
                }
            }
        }

        if (!isSigned) {
            mainNavMenu.notSignInView.visibility = VISIBLE
            mainNavMenu.signInView.visibility = GONE
        }

        binding.hamburgerMenu.setOnClickListener {
            mainActivity.binding.drawerLayout.openDrawer(mainNavMenu.navigationView)
        }

        mainNavMenu.userBtn.setOnClickListener {
            if (!isSigned) {
                getSignInResult.launch(Intent(context, LoginActivity::class.java))
            } else {
                navBtnTest("로그인됨")
            }
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
        naverMap.minZoom = 10.0
        naverMap.locationSource = locationSource

        val uiSettings = naverMap.uiSettings
        uiSettings.isCompassEnabled = false
        uiSettings.isScaleBarEnabled = false
        uiSettings.isZoomControlEnabled = false

        binding.zoom.map = naverMap
        binding.location.map = naverMap
    }

    override fun onClick(overlay: Overlay): Boolean {
        if (overlay is Marker) {
            Toast.makeText(context, "마커 선택됨", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    /**
     * 네비게이션 드로어 버튼 테스트용
     */
    private fun navBtnTest(content: String) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
        mainActivity.binding.drawerLayout.closeDrawers()
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 2001

        fun newInstance() : NearMeFragment {
            return NearMeFragment()
        }
    }
}