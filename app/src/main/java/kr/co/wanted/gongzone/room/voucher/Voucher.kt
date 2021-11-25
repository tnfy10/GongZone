package kr.co.wanted.gongzone.room.voucher

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "voucher")
data class Voucher(
    @PrimaryKey
    @ColumnInfo
    var name: String,
    @ColumnInfo var kind: String,
    @ColumnInfo var time: Int,
    @ColumnInfo var day: Int,
    @ColumnInfo var price: Int
)