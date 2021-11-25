package kr.co.wanted.gongzone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.wanted.gongzone.databinding.VoucherItemBinding
import kr.co.wanted.gongzone.room.voucher.Voucher
import java.text.DecimalFormat

class VoucherAdapter: RecyclerView.Adapter<VoucherAdapter.Holder>() {
    var listData = ArrayList<Voucher>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = VoucherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val voucher = listData[position]

        holder.setVoucher(voucher.name, voucher.price)
    }

    fun getCheckedItemList(): ArrayList<String> {
        return checkedItemList
    }

    fun clearCheckedItemList() {
        checkedItemList.clear()
    }

    class Holder(val binding: VoucherItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.checkedTextView.setOnClickListener {
                val obj = it as CheckedTextView
                obj.isChecked = obj.isChecked != true
                if (obj.isChecked) checkedItemList.add(obj.text.toString())
                else checkedItemList.remove(obj.text.toString())
            }
        }

        fun setVoucher(name: String, price: Int) {
            binding.checkedTextView.text = name
            "${DecimalFormat("###,###,###").format(price)}원".also { binding.priceTxt.text = it }

            if (checkedItemList.isNotEmpty()) {
                for (item in checkedItemList)
                    if (binding.checkedTextView.text == item)
                        binding.checkedTextView.isChecked = true
            }
        }
    }

    companion object {
        val checkedItemList = ArrayList<String>()
    }
}