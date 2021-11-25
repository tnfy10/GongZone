package kr.co.wanted.gongzone.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import kr.co.wanted.gongzone.room.voucher.Voucher
import kr.co.wanted.gongzone.room.voucher.VoucherDB

class VoucherViewModel: ViewModel() {
    private val voucherList = MutableLiveData<List<Voucher>>()
    private val selectedVoucherList = MutableLiveData<List<Voucher>>()
    private var db: VoucherDB? = null

    fun setVoucherList(context: Context) {
        db = Room.databaseBuilder(context, VoucherDB::class.java, "voucher.db")
            .createFromAsset("database/voucher.db")
            .build()
        voucherList.postValue(db?.voucherDao()?.getVoucherList())
    }

    fun getVoucherList(): LiveData<List<Voucher>> = voucherList

    fun setSelectedVoucherList(list: List<Voucher>) {
        selectedVoucherList.value = list
    }

    fun getSelectedVoucherList(): LiveData<List<Voucher>> = selectedVoucherList
}