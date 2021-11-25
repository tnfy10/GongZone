package kr.co.wanted.gongzone.view.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.adapter.VoucherAdapter
import kr.co.wanted.gongzone.databinding.FragmentProductSelectBinding
import kr.co.wanted.gongzone.room.voucher.Voucher
import kr.co.wanted.gongzone.viewmodel.StoreViewModel
import kr.co.wanted.gongzone.viewmodel.VoucherViewModel

class ProductSelectFragment : Fragment() {

    private lateinit var binding: FragmentProductSelectBinding
    private lateinit var purchaseActivity: PurchaseActivity
    private lateinit var viewModel: VoucherViewModel
    private val oneTimeAdapter = VoucherAdapter()
    private val partTimeAdapter = VoucherAdapter()
    private val periodAdapter = VoucherAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        purchaseActivity = activity as PurchaseActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductSelectBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(purchaseActivity)[VoucherViewModel::class.java]
        viewModel.getVoucherList().observe(viewLifecycleOwner, { voucherList ->
            setVoucherList(voucherList, ONE_TIME)
            setVoucherList(voucherList, PART_TIME)
            setVoucherList(voucherList, PERIOD)

            binding.checkBtn.setOnClickListener {
                val checkedList = getSelectedVoucherList(VoucherAdapter().getCheckedItemList(), voucherList as ArrayList<Voucher>)

                if (checkedList.isEmpty()) {
                    Toast.makeText(context, "이용권을 선택해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.setSelectedVoucherList(checkedList)

                    purchaseActivity.supportFragmentManager.beginTransaction().apply {
                        addToBackStack(null)
                        replace(purchaseActivity.binding.container.id, purchaseActivity.paymentFragment)
                        commit()
                    }
                }
            }
        })
    }

    private fun getSelectedVoucherList(nameList: ArrayList<String>, voucherList: ArrayList<Voucher>): ArrayList<Voucher> {
        val tmp = ArrayList<Voucher>()

        for (voucher in voucherList) {
            for (name in nameList) {
                if (name == voucher.name) tmp.add(voucher)
            }
        }

        return tmp
    }

    private fun setVoucherList(list: List<Voucher>, kind: String) {
        val tmpList = ArrayList<Voucher>()
        val linearLayoutManager = LinearLayoutManager(purchaseActivity)

        for (item in list) {
            if (item.kind == kind) tmpList.add(item)
        }

        when (kind) {
            ONE_TIME -> {
                oneTimeAdapter.listData = tmpList
                binding.oneTimeList.adapter = oneTimeAdapter
                binding.oneTimeList.layoutManager = linearLayoutManager
            }
            PART_TIME -> {
                partTimeAdapter.listData = tmpList
                binding.partTimeList.adapter = partTimeAdapter
                binding.partTimeList.layoutManager = linearLayoutManager
            }
            PERIOD -> {
                periodAdapter.listData = tmpList
                binding.periodList.adapter = periodAdapter
                binding.periodList.layoutManager = linearLayoutManager
            }
        }
    }

    companion object {
        private const val ONE_TIME = "1회"
        private const val PART_TIME = "시간제"
        private const val PERIOD = "기간제"

        fun newInstance(): ProductSelectFragment {
            return ProductSelectFragment()
        }
    }
}