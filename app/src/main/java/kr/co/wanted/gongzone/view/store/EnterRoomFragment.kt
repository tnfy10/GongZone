package kr.co.wanted.gongzone.view.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.FragmentEnterRoomBinding

class EnterRoomFragment : Fragment() {

    private lateinit var binding: FragmentEnterRoomBinding
    private lateinit var storeActivity: StoreActivity
    private lateinit var seatSelectFragment: SeatSelectFragment
    private lateinit var voucherSelectFragment: VoucherSelectFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeActivity = activity as StoreActivity
        seatSelectFragment = SeatSelectFragment.newInstance()
        voucherSelectFragment = VoucherSelectFragment.newInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterRoomBinding.inflate(inflater, container, false)

        childFragmentManager.beginTransaction().apply {
            replace(binding.tabView.id, seatSelectFragment)
            commit()
        }

        binding.backBtn.setOnClickListener {
            storeActivity.onBackPressed()
        }

        deactivateNextBtn()

        binding.nextBtn.setOnClickListener {
            when (childFragmentManager.findFragmentById(binding.tabView.id)) {
                is SeatSelectFragment -> {
                    childFragmentManager.beginTransaction().apply {
                        replace(binding.tabView.id, voucherSelectFragment)
                        addToBackStack(null)
                        commit()
                    }
                    deactivateEnterRoomBtn()
                }
                is VoucherSelectFragment -> {
                    Toast.makeText(context, "미구현", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    fun activateNextBtn() {
        binding.nextBtn.setImageResource(R.drawable.ic_next_btn_activate)
        binding.nextBtn.isClickable = true
    }

    fun deactivateNextBtn() {
        binding.nextBtn.setImageResource(R.drawable.ic_next_btn_deactivate)
        binding.nextBtn.isClickable = false
    }

    fun activateEnterRoomBtn() {
        binding.nextBtn.setImageResource(R.drawable.ic_enter_room_btn_activate)
        binding.nextBtn.isClickable = true
    }

    fun deactivateEnterRoomBtn() {
        binding.nextBtn.setImageResource(R.drawable.ic_enter_room_btn_deactivate)
        binding.nextBtn.isClickable = false
    }

    companion object {
        fun newInstance(): EnterRoomFragment {
            return EnterRoomFragment()
        }
    }
}