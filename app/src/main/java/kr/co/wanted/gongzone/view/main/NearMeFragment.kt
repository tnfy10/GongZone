package kr.co.wanted.gongzone.view.main

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.adapter.FilterViewAdapter
import kr.co.wanted.gongzone.databinding.BottomSheetMainBinding
import kr.co.wanted.gongzone.databinding.FragmentNearMeBinding
import kr.co.wanted.gongzone.databinding.NavMenuMainBinding
import kr.co.wanted.gongzone.model.User
import kr.co.wanted.gongzone.service.UserService
import kr.co.wanted.gongzone.utils.PreferenceManager
import kr.co.wanted.gongzone.utils.Size
import kr.co.wanted.gongzone.view.sign.SignInActivity
import kr.co.wanted.gongzone.view.store.StoreActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class NearMeFragment : Fragment(), IOnFocusListenable, OnMapReadyCallback, Overlay.OnClickListener {

    private lateinit var binding: FragmentNearMeBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var mainNavMenu: NavMenuMainBinding
    private lateinit var mainBottomSheet: BottomSheetMainBinding
    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var getSearchFilterResult: ActivityResultLauncher<Intent>
    private lateinit var behavior: BottomSheetBehavior<View>
    private var bottomSheetHeight: Int = 0
    private var chipIdList = ArrayList<Int>()
    lateinit var mapView: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity)

        getSearchFilterResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                chipIdList = it.data?.getIntegerArrayListExtra("chipIdList") as ArrayList<Int>
                applySearchFilters()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearMeBinding.inflate(inflater, container, false)

        mainActivity.setSupportActionBar(binding.toolbar)
        binding.toolbar.setPadding(0, Size.getStatusBarHeight(resources), 0, 0)

        mainNavMenu = mainActivity.binding.includedNavView
        mainBottomSheet = binding.includedMainBottomSheet
        mapView = binding.mapView
        behavior = BottomSheetBehavior.from(mainBottomSheet.bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        behavior.expandedOffset = Size.getActionBarHeight(mainActivity.theme) + Size.getStatusBarHeight(resources) + Size.dpToPx(context, 50f).toInt()

        binding.hamburgerMenu.setOnClickListener {
            mainActivity.binding.drawerLayout.openDrawer(mainNavMenu.navigationView)
        }

        binding.searchBtn.setOnClickListener {
            startActivity(Intent(context, SearchActivity::class.java))
        }

        binding.notificationBtn.setOnClickListener {
            startActivity(Intent(context, NotificationActivity::class.java))
        }

        binding.filterBtn.setOnClickListener {
            val intent = Intent(context, SearchFilterActivity::class.java)
            intent.putExtra("chipIdList", chipIdList)
            getSearchFilterResult.launch(intent)
        }

        behavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) { }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                setMoveFloatingView(bottomSheet, slideOffset)
            }
        })

        mainBottomSheet.includedGongZonePick.storeDetailBtn.setOnClickListener {
            startActivity(Intent(context, StoreActivity::class.java))
        }

        mainNavMenu.userBtn.setOnClickListener {
            if(checkSigned()) {
                Toast.makeText(context, "로그인됨", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(context, SignInActivity::class.java))
            }
        }

        mainNavMenu.useHistoryBtn.setOnClickListener {

        }

        mainNavMenu.ticketPurchaseBtn.setOnClickListener {

        }

        mainNavMenu.ticketPurchaseHistoryBtn.setOnClickListener {

        }

        mainNavMenu.favoritesBtn.setOnClickListener {

        }

        mainNavMenu.couponBtn.setOnClickListener {

        }

        mainNavMenu.noticeBtn.setOnClickListener {

        }

        mainNavMenu.customerServiceBtn.setOnClickListener {

        }

        mainNavMenu.settingBtn.setOnClickListener {

        }

        showMapFragment()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getUserInfo()
        applySearchFilters()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        bottomSheetHeight = mainBottomSheet.bottomSheet.height
        setFloatingViewPadding()
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
        uiSettings.isTiltGesturesEnabled = false
        uiSettings.isRotateGesturesEnabled = false

        val bottom = Size.dpToPx(context, 70f).toInt()
        val right = Size.dpToPx(context, 16f).toInt()
        uiSettings.setLogoMargin(0, 0, right, bottom)
        uiSettings.logoGravity = Gravity.END or Gravity.BOTTOM

        setFloatingViewPadding()

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
     * floatingView 패딩 설정
     */
    private fun setFloatingViewPadding() {
        var bottom = 0f

        when (behavior.state) {
            BottomSheetBehavior.STATE_COLLAPSED -> { }
            BottomSheetBehavior.STATE_DRAGGING -> { }
            BottomSheetBehavior.STATE_EXPANDED -> {
                bottom = bottomSheetHeight / 1.2f
            }
            BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                bottom = bottomSheetHeight * behavior.halfExpandedRatio / 1.2f
            }
            BottomSheetBehavior.STATE_HIDDEN -> { }
            BottomSheetBehavior.STATE_SETTLING -> { }
        }

        naverMap.setContentPadding(0, 0, 0, bottom.roundToInt())
        binding.floatingView.setPadding(0, 0, 0, bottom.roundToInt())
    }

    /**
     * floatingView 동적 패딩 설정
     */
    private fun setMoveFloatingView(bottomSheet: View, slideOffset: Float) {
        val value = 1.2f
        val off = bottomSheet.height * slideOffset / value
        if (slideOffset <= 0.38f) {
            naverMap.setContentPadding(0, 0, 0, (off/(value-0.1f)).roundToInt())
            binding.floatingView.setPadding(0, 0, 0, off.roundToInt())
        }
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
     * 로그인 여부 확인
     */
    private fun checkSigned()
    = PreferenceManager.getBoolean(mainActivity, "isSigned")

    /**
     * 검색 필터 적용
     */
    private fun applySearchFilters() {
        val adapter = FilterViewAdapter()
        adapter.listData = chipsIdToText()
        binding.filterRecyclerView.adapter = adapter
        binding.filterRecyclerView.layoutManager = LinearLayoutManager(
            mainActivity, LinearLayoutManager.HORIZONTAL, false)
        if (adapter.itemCount != 0) binding.filterView.visibility = VISIBLE
        else binding.filterView.visibility = GONE
    }

    /**
     * 필터 id를 문자로 변경
     */
    private fun chipsIdToText(): ArrayList<String> {
        val chips = ArrayList<String>()

        for (id in chipIdList) {
            when (id) {
                R.id.currentEnterPossibleChip ->
                    chips.add(resources.getString(R.string.current_enter_possible))
                R.id.pointFourHigherChip ->
                    chips.add(resources.getString(R.string.point_4_higher))
                R.id.squareTableChip ->
                    chips.add(resources.getString(R.string.square_table))
                R.id.individualStandChip ->
                    chips.add(resources.getString(R.string.individual_stand))
                R.id.whiteLightChip ->
                    chips.add(resources.getString(R.string.white_light))
                R.id.socketChip ->
                    chips.add(resources.getString(R.string.socket))
                R.id.backrestChairChip ->
                    chips.add(resources.getString(R.string.backrest_chair))
                R.id.restroomChip ->
                    chips.add(resources.getString(R.string.restroom))
                R.id.smallTableChip ->
                    chips.add(resources.getString(R.string.small))
                R.id.mediumTableChip ->
                    chips.add(resources.getString(R.string.medium))
                R.id.largeTableChip ->
                    chips.add(resources.getString(R.string.large))
                R.id.chargerChip ->
                    chips.add(resources.getString(R.string.charger))
                R.id.readingPropChip ->
                    chips.add(resources.getString(R.string.reading_prop))
                R.id.blanketChip ->
                    chips.add(resources.getString(R.string.blanket))
            }
        }

        return chips
    }

    /**
     * 사용자 정보 로드
     */
    private fun getUserInfo() {
        if (checkSigned()) {
            val id = PreferenceManager.getString(mainActivity, "userId")
            val pwd = PreferenceManager.getString(mainActivity, "pwd")

            if (id!=null && pwd!=null) {
                UserService.create().getUser(id, pwd).enqueue(object: Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        val user = response.body()?.get(0)

                        if (user!=null) {
                            mainNavMenu.userNameTxt.text = user.name
                            mainNavMenu.userIdTxt.text = user.userId
                            mainNavMenu.signInView.visibility = VISIBLE
                            mainNavMenu.notSignInView.visibility = GONE
                        } else {
                            mainNavMenu.signInView.visibility = GONE
                            mainNavMenu.notSignInView.visibility = VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.d(TAG, "통신실패: ${t.message}")
                        mainNavMenu.signInView.visibility = GONE
                        mainNavMenu.notSignInView.visibility = VISIBLE
                    }

                })
            }
        }
    }

    companion object {
        private const val TAG = "NearMeFragment"

        fun newInstance() : NearMeFragment {
            return NearMeFragment()
        }
    }
}