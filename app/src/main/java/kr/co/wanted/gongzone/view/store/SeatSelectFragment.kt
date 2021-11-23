package kr.co.wanted.gongzone.view.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.FragmentSeatSelectBinding

class SeatSelectFragment : Fragment() {

    private lateinit var binding: FragmentSeatSelectBinding
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
        binding = FragmentSeatSelectBinding.inflate(inflater, container, false)

        enterRoomFragment.deactivateNextBtn()

        binding.testBtn.setOnClickListener {
            enterRoomFragment.activateNextBtn()
        }

        return binding.root
    }

    companion object {
        fun newInstance(): SeatSelectFragment {
            return SeatSelectFragment()
        }
    }
}