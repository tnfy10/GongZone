package kr.co.wanted.gongzone.view.store

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.FragmentSpaceGuideBinding
import kr.co.wanted.gongzone.utils.Size
import kr.co.wanted.gongzone.viewmodel.StoreViewModel
import java.time.LocalTime

class SpaceGuideFragment : Fragment() {

    private lateinit var binding: FragmentSpaceGuideBinding
    private lateinit var viewModel: StoreViewModel
    private lateinit var storeActivity: StoreActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeActivity = activity as StoreActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpaceGuideBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(storeActivity)[StoreViewModel::class.java]
        viewModel.getSpaceLiveData().observe(viewLifecycleOwner, { spaceItem ->
            val totalSeat = spaceItem.totalSeatS.toInt() + spaceItem.totalSeatM.toInt() + spaceItem.totalSeatL.toInt()
            val leastSeat = spaceItem.leastSeatS.toInt() + spaceItem.leastSeatM.toInt() + spaceItem.leastSeatL.toInt()
            "$leastSeat/$totalSeat".also { binding.numOfRemainSeatTxt.text = it }

            val currentTime = "(${LocalTime.now().hour}:${LocalTime.now().minute} 기준)"
            binding.basedOnCurrentTimeTxt.text = currentTime

            binding.numOfRemainSmallSeatTxt.text = spaceItem.leastSeatS
            binding.numOfRemainMediumSeatTxt.text = spaceItem.leastSeatM
            binding.numOfRemainLargeSeatTxt.text = spaceItem.leastSeatL

            binding.storeInfoTxt.text = spaceItem.info
            "${spaceItem.openTime}~${spaceItem.closeTime}".also { binding.businessHourTxt.text = it }
            binding.storeTelTxt.text = spaceItem.phone

            for (str in spaceItem.notice.split(";;")) {
                val textView = TextView(storeActivity)
                "· $str".also { textView.text = it }
                textView.textSize = 14f
                textView.setTextColor(resources.getColor(R.color.gray_800, null))
                textView.typeface = Typeface.create("noto_sans_cjk_kr_regular", Typeface.NORMAL)
                textView.includeFontPadding = false

                val params = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                params.setMargins(0, 0, 0, Size.dpToPx(storeActivity, 3f).toInt())
                textView.layoutParams = params
                binding.noticeAndEventContent.addView(textView)
            }
        })
    }

    companion object {
        fun newInstance(): SpaceGuideFragment{
            return SpaceGuideFragment()
        }
    }
}