package kr.co.wanted.gongzone.view.main

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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
import kr.co.wanted.gongzone.model.space.Space
import kr.co.wanted.gongzone.model.user.User
import kr.co.wanted.gongzone.model.user.UserItem
import kr.co.wanted.gongzone.model.voucher.Voucher
import kr.co.wanted.gongzone.service.SpaceService
import kr.co.wanted.gongzone.service.UserService
import kr.co.wanted.gongzone.utils.PreferenceManager
import kr.co.wanted.gongzone.utils.Size
import kr.co.wanted.gongzone.view.payment.PurchaseActivity
import kr.co.wanted.gongzone.view.sign.SignInActivity
import kr.co.wanted.gongzone.view.store.StoreActivity
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
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
    private var seatNum: String? = null
    private var user: UserItem? = null
    private var voucherNum: String? = null
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
        mainBottomSheet.includedGongZonePick.monthOfBestImg.visibility = VISIBLE
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

        binding.leaveOutBtn.setOnClickListener {
            showExitRoomPopUp()
        }

        behavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) { }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                setMoveFloatingView(bottomSheet, slideOffset)
            }
        })

        mainNavMenu.userBtn.setOnClickListener {
            if(checkSigned()) {
                Toast.makeText(context, "로그인됨", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(context, SignInActivity::class.java))
            }
        }

        mainNavMenu.useHistoryBtn.setOnClickListener {
            Toast.makeText(context, "이용내역", Toast.LENGTH_SHORT).show()
        }

        mainNavMenu.voucherPurchaseBtn.setOnClickListener {
            if (checkSigned()) {
                startActivity(Intent(context, PurchaseActivity::class.java))
            } else {
                Toast.makeText(context, "로그인 후 이용가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        mainNavMenu.voucherPurchaseHistoryBtn.setOnClickListener {
            Toast.makeText(context, "이용권 구매내역", Toast.LENGTH_SHORT).show()
        }

        mainNavMenu.favoritesBtn.setOnClickListener {
            Toast.makeText(context, "관심목록", Toast.LENGTH_SHORT).show()
        }

        mainNavMenu.noticeBtn.setOnClickListener {
            Toast.makeText(context, "공지사항", Toast.LENGTH_SHORT).show()
        }

        mainNavMenu.customerServiceBtn.setOnClickListener {
            Toast.makeText(context, "고객센터", Toast.LENGTH_SHORT).show()
        }

        mainNavMenu.settingBtn.setOnClickListener {
            Toast.makeText(context, "설정", Toast.LENGTH_SHORT).show()
        }

        showMapFragment()
        showGongZonePick()

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
                        val result = response.body()?.get(0)

                        if (result!=null) {
                            user = result
                            mainNavMenu.userNameTxt.text = result.name
                            mainNavMenu.userIdTxt.text = result.userId
                            mainNavMenu.signInView.visibility = VISIBLE
                            mainNavMenu.notSignInView.visibility = GONE

                            userNum = result.userNum
                            seatNum = result.seatNum

                            if (seatNum.isNullOrEmpty()) {
                                binding.leaveOutBtn.visibility = GONE
                            } else {
                                binding.leaveOutBtn.visibility = VISIBLE
                            }
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

    /**
     * 사용중인 이용권 정보 확인
     */
    private fun getVoucherInfo(userNum: String) {
        SpaceService.create().getUserVoucherList(userNum).enqueue(object: Callback<Voucher>{
            override fun onResponse(call: Call<Voucher>, response: Response<Voucher>) {
                val voucherList = response.body()

                if (voucherList != null) {
                    for (item in voucherList) {
                        if (item.nowUsing == "1") {
                            voucherNum = item.voucherNum
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Voucher>, t: Throwable) {
                Log.d(TAG, "통신실패: ${t.message}")
            }

        })
    }

    /**
     * 퇴실하기
     */
    private fun exitRoom(dialog: Dialog) {
        val us = user

        if (us != null) {
            getVoucherInfo(us.userNum)
            val vNum = voucherNum
            if (vNum != null) {
                val dateTime = PreferenceManager.getString(mainActivity, "enterRoomDateTime")
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val enterRoomDateTime = LocalDateTime.parse(dateTime, formatter)
                val nowDateTime = LocalDateTime.now()
                val useTime = ChronoUnit.MINUTES.between(enterRoomDateTime, nowDateTime).toString().toInt()
                val initialAvailableTime =
                    PreferenceManager.getString(mainActivity, "availableTime")?.toInt()?.times(60)

                val availableTime = initialAvailableTime?.minus(useTime).toString()

                SpaceService.create().exitRoom(us.seatNum, us.userNum, vNum, availableTime).enqueue(object: Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>,
                    ) {
                        val result = response.body()?.string()

                        if (response.isSuccessful) {
                            when (result) {
                                "1" -> {
                                    dialog.dismiss()
                                    showExitRoomSuccessPopUp()
                                    binding.leaveOutBtn.visibility = GONE
                                }
                                else -> {
                                    dialog.dismiss()
                                    Toast.makeText(mainActivity, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            dialog.dismiss()
                            Toast.makeText(mainActivity, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d(TAG, "통신실패: ${t.message}")
                        dialog.dismiss()
                    }

                })
            }
        }
    }

    /**
     * 공존픽 로드
     */
    private fun showGongZonePick() {
        SpaceService.create().getSpace("1").enqueue(object: Callback<Space>{
            override fun onResponse(call: Call<Space>, response: Response<Space>) {
                val spaceList = response.body()

                if (spaceList != null) {
                    val space = spaceList[0]
                    mainBottomSheet.includedGongZonePick.storeIntroTxt.text = space.introduce
                    "${space.spaceName}·${space.spaceType}".also { mainBottomSheet.includedGongZonePick.storeNameAndKindTxt.text = it }
                    Glide.with(mainActivity).load(space.imagePath).into(mainBottomSheet.includedGongZonePick.storeImg)
                    mainBottomSheet.includedGongZonePick.storeDetailBtn.setOnClickListener {
                        val intent = Intent(context, StoreActivity::class.java)
                        intent.putExtra("spaceNum", space.spaceNum)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<Space>, t: Throwable) {
                Log.d(TAG, "통신실패: ${t.message}")
            }

        })
    }

    /**
     * 퇴실하기 팝업
     */
    private fun showExitRoomPopUp() {
        val dialog = Dialog(mainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_yes_no)

        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val cancelBtn = dialog.findViewById<ImageButton>(R.id.cancelBtn)
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        val okBtn = dialog.findViewById<ImageButton>(R.id.okBtn)
        okBtn.setOnClickListener {
            exitRoom(dialog)
        }

        val popUpTxt = dialog.findViewById<TextView>(R.id.popUpTxt)
        "퇴실 버튼을 누르면 이용이\n종료됩니다. 퇴실하시겠습니까?".also { popUpTxt.text = it }
    }

    /**
     * 퇴실 완료 팝업
     */
    private fun showExitRoomSuccessPopUp() {
        val dialog = Dialog(mainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_end)

        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val closeBtn = dialog.findViewById<ImageButton>(R.id.closeBtn)
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        val popUpTxt = dialog.findViewById<TextView>(R.id.popUpTxt)
        popUpTxt.text = "이용이 종료되었습니다."
    }

    companion object {
        private const val TAG = "NearMeFragment"
        var userNum: String? = null

        fun newInstance() : NearMeFragment {
            return NearMeFragment()
        }
    }
}