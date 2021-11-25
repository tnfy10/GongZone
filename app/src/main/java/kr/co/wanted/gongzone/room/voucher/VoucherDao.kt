package kr.co.wanted.gongzone.room.voucher

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface VoucherDao {
    @Query("SELECT * FROM voucher")
    fun getVoucherList(): List<Voucher>
}