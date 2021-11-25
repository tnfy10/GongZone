package kr.co.wanted.gongzone.room.voucher

import androidx.room.*

@Database(entities = [Voucher::class], version = 1, exportSchema = false)
abstract class VoucherDB: RoomDatabase() {

    abstract fun voucherDao(): VoucherDao
}