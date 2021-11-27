package kr.co.wanted.gongzone.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.wanted.gongzone.model.seat.Seat
import kr.co.wanted.gongzone.model.seat.SeatItem
import kr.co.wanted.gongzone.model.space.Space
import kr.co.wanted.gongzone.model.space.SpaceItem
import kr.co.wanted.gongzone.model.voucher.Voucher
import kr.co.wanted.gongzone.model.voucher.VoucherItem
import kr.co.wanted.gongzone.service.SpaceService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreViewModel: ViewModel() {
    private val spaceLiveData = MutableLiveData<SpaceItem>()
    private val seatLiveData = MutableLiveData<Seat>()
    private val voucherLiveData = MutableLiveData<Voucher>()
    private lateinit var seatItem: SeatItem
    private lateinit var selectVoucher: VoucherItem

    private fun loadSpaceInfo(spaceNum: String) {
        SpaceService.create().getSpace(spaceNum).enqueue(object: Callback<Space> {
            override fun onResponse(call: Call<Space>, response: Response<Space>) {
                val spaceList = response.body()

                if (spaceList != null) {
                    spaceLiveData.value = spaceList[0]
                }
            }

            override fun onFailure(call: Call<Space>, t: Throwable) {
                Log.d("공간정보 로드", "통신실패: ${t.message}")
            }
        })
    }

    private fun loadSeatsInfo(spaceNum: String) {
        SpaceService.create().getSeatsInfo(spaceNum).enqueue(object: Callback<Seat>{
            override fun onResponse(call: Call<Seat>, response: Response<Seat>) {
                val seat = response.body()

                if (seat != null) {
                    seatLiveData.value = seat
                }
            }

            override fun onFailure(call: Call<Seat>, t: Throwable) {
                Log.d("시트정보 로드", "통신실패: ${t.message}")
            }
        })
    }

    private fun loadUserVoucherList(userNum: String) {
        SpaceService.create().getUserVoucherList(userNum).enqueue(object: Callback<Voucher>{
            override fun onResponse(call: Call<Voucher>, response: Response<Voucher>) {
                val voucher = response.body()

                if (voucher != null) {
                    voucherLiveData.value = voucher
                }
            }

            override fun onFailure(call: Call<Voucher>, t: Throwable) {
                Log.d("이용권 정보 로드", "통신실패: ${t.message}")
            }
        })
    }

    fun setSpaceLiveData(spaceNum: String) {
        loadSpaceInfo(spaceNum)
    }

    fun getSpaceLiveData(): LiveData<SpaceItem> = spaceLiveData

    fun setSeatLiveData(spaceNum: String) {
        loadSeatsInfo(spaceNum)
    }

    fun getSeatLiveData(): LiveData<Seat> = seatLiveData

    fun setUserVoucherListLiveData(userNum: String) {
        loadUserVoucherList(userNum)
    }

    fun getUserVoucherListLiveData(): LiveData<Voucher> = voucherLiveData

    fun setSeatItem(seatNum: Int) {
        val seat = getSeatLiveData().value

        if (seat != null) {
            for (item in seat) {
                if (item.seatNum.toInt() == seatNum) {
                    seatItem = item
                }
            }
        }
    }

    fun getSeatItem() = seatItem

    fun setVoucher(voucher: VoucherItem) {
        selectVoucher = voucher
    }

    fun getVoucher() = selectVoucher
}