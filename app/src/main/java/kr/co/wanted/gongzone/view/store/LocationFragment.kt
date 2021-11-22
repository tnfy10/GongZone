package kr.co.wanted.gongzone.view.store

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kr.co.wanted.gongzone.BuildConfig
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.FragmentLocationBinding
import kr.co.wanted.gongzone.model.geocode.Geocode
import kr.co.wanted.gongzone.service.NaverAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentLocationBinding
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var geocode: Geocode

    private val address = "경기 부천시 부천로 1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationBinding.inflate(inflater, container, false)

        binding.findWayBtn.setOnClickListener {
            val url = "geo:${geocode.addresses[0].y},${geocode.addresses[0].x}?q=${address}"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        naverMap.maxZoom = 18.0
        naverMap.minZoom = 5.0

        val uiSettings = naverMap.uiSettings
        uiSettings.isCompassEnabled = false
        uiSettings.isScaleBarEnabled = false
        uiSettings.isZoomControlEnabled = false
        uiSettings.isTiltGesturesEnabled = false
        uiSettings.isRotateGesturesEnabled = false

        getGeocode(address)
    }

    private fun getGeocode(address: String) {
        NaverAPI.create().getGeocode(
            BuildConfig.NAVER_MAP_CLIENT_ID,
            BuildConfig.NAVER_MAP_CLIENT_SECRET,
            NaverAPI.accept, address).enqueue(object: Callback<Geocode> {
            override fun onResponse(call: Call<Geocode>, response: Response<Geocode>) {
                if (response.isSuccessful) {
                    val tmp = response.body()

                    if (tmp != null) {
                        geocode = tmp

                        val x = geocode.addresses[0].x.toDouble()
                        val y = geocode.addresses[0].y.toDouble()
                        val marker = Marker()
                        marker.icon = OverlayImage.fromResource(R.drawable.ic_pin_select)
                        marker.position = LatLng(y, x)
                        marker.map = naverMap

                        val cameraPosition = CameraPosition(marker.position, 15.0)
                        val cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition).animate(
                            CameraAnimation.Easing)
                        naverMap.moveCamera(cameraUpdate)
                    } else {
                        Log.d(TAG, "주소 정보 없음")
                    }

                } else {
                    Log.d(TAG, response.message())
                }
            }

            override fun onFailure(call: Call<Geocode>, t: Throwable) {
                Log.d(TAG, "통신실패: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "LocationFragment"

        fun newInstance(): LocationFragment{
            return LocationFragment()
        }
    }
}