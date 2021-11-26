package kr.co.wanted.gongzone.view.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.FragmentVoucherSelectBinding
import kr.co.wanted.gongzone.viewmodel.StoreViewModel

class VoucherSelectFragment : Fragment() {

    private lateinit var binding: FragmentVoucherSelectBinding
    private lateinit var storeActivity: StoreActivity
    private lateinit var viewModel: StoreViewModel
    private lateinit var enterRoomFragment: EnterRoomFragment
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


        //enterRoomFragment.activateEnterRoomBtn()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(storeActivity)[StoreViewModel::class.java]
        viewModel.getSpaceLiveData().observe(viewLifecycleOwner, { spaceItem ->
            binding.storeIntroTxt.text = spaceItem.introduce
            binding.storeNameTxt.text = spaceItem.name
            "${spaceItem.openTime}~${spaceItem.closeTime}".also { binding.useTimeTxt.text = it }

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
    }

    companion object {
        fun newInstance(): VoucherSelectFragment {
            return VoucherSelectFragment()
        }
    }
}