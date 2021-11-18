package kr.co.wanted.gongzone.view.main

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentContainerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.BottomSheetMainBinding
import kr.co.wanted.gongzone.databinding.FragmentNearMeBinding
import kr.co.wanted.gongzone.databinding.NavMenuMainBinding
import kr.co.wanted.gongzone.utils.Size
import kr.co.wanted.gongzone.view.sign.SignInActivity

class NearMeFragment : Fragment(), OnMapReadyCallback, Overlay.OnClickListener {

    private lateinit var binding: FragmentNearMeBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var mainNavMenu: NavMenuMainBinding
    private lateinit var mainBottomSheet: BottomSheetMainBinding
    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var getSignInResult: ActivityResultLauncher<Intent>
    private lateinit var getSearchFilterResult: ActivityResultLauncher<Intent>
    private lateinit var chips: ArrayList<String>
    lateinit var behavior: BottomSheetBehavior<View>
    lateinit var mapView: FragmentContainerView
    private var isSigned = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = (activity as MainActivity)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearMeBinding.inflate(inflater, container, false)

        mainActivity.setSupportActionBar(binding.toolbar)
        binding.toolbar.setPadding(0, Size.getStatusBarHeight(resources), 0, 0)

        showMapFragment()

        mainNavMenu = mainActivity.binding.includedNavView
        mainBottomSheet = binding.includedMainBottomSheet
        mapView = binding.mapView
        behavior = BottomSheetBehavior.from(mainBottomSheet.bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        behavior.expandedOffset = Size.getActionBarHeight(mainActivity.theme) + Size.getStatusBarHeight(resources) + Size.dpToPx(context, 50f).toInt()

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

        getSearchFilterResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                chips = it.data?.getStringArrayListExtra("selectedChips") as ArrayList<String>
                applySearchFilters()
            }
        }

        binding.hamburgerMenu.setOnClickListener {
            mainActivity.binding.drawerLayout.openDrawer(mainNavMenu.navigationView)
        }

        binding.notificationBtn.setOnClickListener {
            startActivity(Intent(context, NotificationActivity::class.java))
        }

        binding.filterBtn.setOnClickListener {
            getSearchFilterResult.launch(Intent(context, SearchFilterActivity::class.java))
        }

        checkSigned()
        setNavigationItem()

        return binding.root
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        naverMap.maxZoom = 18.0
        naverMap.minZoom = 5.0

        setCameraPosition()

        val uiSettings = naverMap.uiSettings
        uiSettings.isCompassEnabled = false
        uiSettings.isScaleBarEnabled = false
        uiSettings.isZoomControlEnabled = false

        binding.zoomOutBtn.setOnClickListener {
            val zoomOut = CameraUpdate.zoomOut()
            zoomOut.animate(CameraAnimation.Fly)
            naverMap.moveCamera(zoomOut)
        }

        binding.zoomInBtn.setOnClickListener {
            val zoomIn = CameraUpdate.zoomIn()
            zoomIn.animate(CameraAnimation.Fly)
            naverMap.moveCamera(zoomIn)
        }

        binding.locationBtn.setOnClickListener {
            setCameraPosition()
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }
    }

    override fun onClick(overlay: Overlay): Boolean {
        if (overlay is Marker) {
            Toast.makeText(context, "마커 선택됨", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    /**
     * 네이버지도 표시
     */
    private fun showMapFragment() {
        val fm = mainActivity.supportFragmentManager
        val mapFragment: MapFragment? = MapFragment.newInstance()
        mapFragment?.let { fm.beginTransaction().add(R.id.mapView, it).commit() }
        mapFragment?.getMapAsync(this)
    }

    /**
     * 네이버지도 카메라 포지션 설정
     */
    @SuppressLint("MissingPermission")
    private fun setCameraPosition() {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            val latLng = LatLng(it)
            val cameraPosition = CameraPosition(latLng, 15.0)
            val cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition).animate(CameraAnimation.Easing)
            naverMap.moveCamera(cameraUpdate)

            val locationOverlay = naverMap.locationOverlay
            locationOverlay.icon = OverlayImage.fromResource(R.drawable.ic_my_location)
            locationOverlay.position = latLng
            locationOverlay.isVisible = true
        }
    }

    /**
     * 네비게이션 메뉴 항목별 작동 설정
     */
    private fun setNavigationItem() {
        mainNavMenu.userBtn.setOnClickListener {
            if(isSigned) {
                navBtnTest("로그인됨")
            } else {
                getSignInResult.launch(Intent(context, SignInActivity::class.java))
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
    }

    /**
     * 로그인 여부 확인
     */
    private fun checkSigned() {
        if (isSigned) {
            mainNavMenu.notSignInView.visibility = GONE
            mainNavMenu.signInView.visibility = VISIBLE
        } else {
            mainNavMenu.notSignInView.visibility = VISIBLE
            mainNavMenu.signInView.visibility = GONE
        }
    }

    /**
     * 검색 필터 적용
     */
    private fun applySearchFilters() {
        for (text in chips) {
            Log.d("chiptest", text)
        }
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