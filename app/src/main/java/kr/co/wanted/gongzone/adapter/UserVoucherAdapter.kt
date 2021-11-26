package kr.co.wanted.gongzone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.UserVoucherItemBinding
import kr.co.wanted.gongzone.databinding.VoucherItemBinding
import kr.co.wanted.gongzone.model.voucher.Voucher
import kr.co.wanted.gongzone.model.voucher.VoucherItem
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class UserVoucherAdapter: RecyclerView.Adapter<UserVoucherAdapter.Holder>() {
    var listData = ArrayList<VoucherItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = UserVoucherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val voucher = listData[position]

        holder.setVoucher(voucher)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, pos: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class Holder(val binding: UserVoucherItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.voucherBtn.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(it, pos)
                }
            }
        }

        fun setVoucher(voucher: VoucherItem) {
            when (voucher.type) {
                "once" -> {
                    "1회 이용권 : ${voucher.initialTime}시간".also { binding.voucherNameTxt.text = it }
                    "${voucher.initialDate}~${voucher.availableDate}".also { binding.availablePeriodTxt.text = it }
                    "${voucher.initialTime} 시간".also { binding.availableTimeTxt.text = it }
                    "잔여시간 ${voucher.availableTime}시간".also { binding.voucherUseStatusTxt.text = it }
                }
                "time" -> {
                    "시간제 이용권 : ${voucher.initialTime}시간".also { binding.voucherNameTxt.text = it }
                    "${voucher.initialDate}~${voucher.availableDate}".also { binding.availablePeriodTxt.text = it }
                    "${voucher.initialTime} 시간".also { binding.availableTimeTxt.text = it }
                    "잔여시간 ${voucher.availableTime}시간".also { binding.voucherUseStatusTxt.text = it }
                }
                "period" -> {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val initialDate = LocalDate.parse(voucher.initialDate, formatter)
                    val availableDate = LocalDate.parse(voucher.availableDate, formatter)
                    val period = Period.between(initialDate, availableDate)
                    val week = period.days/7

                    "기간제 이용권 : ${week}주".also { binding.voucherNameTxt.text = it }
                    "${voucher.initialDate}~${voucher.availableDate}".also { binding.availablePeriodTxt.text = it }
                    binding.availableTimeTxt.text = "-"
                    binding.voucherUseStatusTxt.text = "-"
                }
            }
        }
    }
}