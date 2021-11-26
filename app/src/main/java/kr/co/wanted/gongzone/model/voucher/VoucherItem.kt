package kr.co.wanted.gongzone.model.voucher

data class VoucherItem(
    val availableDate: String,
    val availableTime: String,
    val initialDate: String,
    val initialTime: String,
    val nowUsing: String,
    val type: String,
    val userNum: String,
    val voucherNum: String
)