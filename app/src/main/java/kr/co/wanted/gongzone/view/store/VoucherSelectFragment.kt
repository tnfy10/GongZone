package kr.co.wanted.gongzone.view.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.FragmentVoucherSelectBinding

class VoucherSelectFragment : Fragment() {

    private lateinit var binding: FragmentVoucherSelectBinding
    private lateinit var storeActivity: StoreActivity
    private lateinit var enterRoomFragment: EnterRoomFragment

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

        binding.testBtn.setOnClickListener {
            enterRoomFragment.activateEnterRoomBtn()
        }

        return binding.root
    }

    companion object {
        fun newInstance(): VoucherSelectFragment {
            return VoucherSelectFragment()
        }
    }
}