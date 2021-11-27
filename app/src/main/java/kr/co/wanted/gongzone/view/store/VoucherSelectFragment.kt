package kr.co.wanted.gongzone.view.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.adapter.UserVoucherAdapter
import kr.co.wanted.gongzone.databinding.FragmentVoucherSelectBinding
import kr.co.wanted.gongzone.model.voucher.VoucherItem
import kr.co.wanted.gongzone.viewmodel.StoreViewModel

class VoucherSelectFragment : Fragment() {

    private lateinit var binding: FragmentVoucherSelectBinding
    private lateinit var storeActivity: StoreActivity
    private lateinit var viewModel: StoreViewModel
    private lateinit var enterRoomFragment: EnterRoomFragment
    private var selectedItem: VoucherItem? = null
    private var selectedPos: Int = -1
    private val alphabet = arrayOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
        "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
        "W", "X", "Y", "Z")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeActivity = activity as StoreActivity
        enterRoomFragment = storeActivity.supportFragmentManager.findFragmentById(R.id.container) as EnterRoomFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVoucherSelectBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(storeActivity)[StoreViewModel::class.java]
        viewModel.getSpaceLiveData().observe(viewLifecycleOwner, { spaceItem ->
            binding.storeIntroTxt.text = spaceItem.introduce
            binding.storeNameTxt.text = spaceItem.spaceName

            val seat = viewModel.getSeatItem()

            when (seat.type) {
                "S" -> {
                    val str = "스몰${alphabet[seat.seatLocate.toInt()]}"
                    binding.selectSeatTxt.text = str
                }
                "M" -> {
                    val str = "미디움${alphabet[seat.seatLocate.toInt()]}"
                    binding.selectSeatTxt.text = str
                }
                "L" -> {
                    val str = "라지${alphabet[seat.seatLocate.toInt()]}"
                    binding.selectSeatTxt.text = str
                }
            }
        })

        viewModel.getUserVoucherListLiveData().observe(viewLifecycleOwner, {
            val voucherList: ArrayList<VoucherItem> = it
            val adapter = UserVoucherAdapter()
            adapter.listData = voucherList
            adapter.setOnItemClickListener(object: UserVoucherAdapter.OnItemClickListener{
                override fun onItemClick(v: View, pos: Int) {
                    if (selectedPos == -1) {
                        selectedItem = adapter.listData[pos]
                        selectedPos = pos
                        v.setBackgroundResource(R.drawable.user_voucher_selected)
                    } else if (selectedPos != pos) {
                        binding.voucherRecyclerView[selectedPos]
                            .setBackgroundResource(R.drawable.user_voucher_nonselected)

                        selectedItem = adapter.listData[pos]
                        selectedPos = pos
                        v.setBackgroundResource(R.drawable.user_voucher_selected)
                    }

                    selectedItem?.let { vc -> viewModel.setVoucher(vc) }
                    enterRoomFragment.activateEnterRoomBtn()
                }
            })
            binding.voucherRecyclerView.adapter = adapter
            binding.voucherRecyclerView.layoutManager = LinearLayoutManager(storeActivity)
        })
    }

    companion object {
        fun newInstance(): VoucherSelectFragment {
            return VoucherSelectFragment()
        }
    }
}