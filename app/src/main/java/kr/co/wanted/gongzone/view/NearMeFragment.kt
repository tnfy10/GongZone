package kr.co.wanted.gongzone.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.FragmentNearMeBinding

class NearMeFragment : Fragment() {

    private lateinit var binding: FragmentNearMeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearMeBinding.inflate(inflater, container, false)

        val slidePanel = binding.mainFrame
        slidePanel.addPanelSlideListener(PanelEventListener())

        return binding.root
    }

    companion object {
        fun newInstance() : NearMeFragment {
            return NearMeFragment()
        }
    }

    inner class PanelEventListener : SlidingUpPanelLayout.PanelSlideListener {
        // 패널이 슬라이드 중일 때
        override fun onPanelSlide(panel: View?, slideOffset: Float) {
            binding.toggleBtn.visibility = VISIBLE
        }

        // 패널의 상태가 변했을 때
        override fun onPanelStateChanged(
            panel: View?,
            previousState: SlidingUpPanelLayout.PanelState?,
            newState: SlidingUpPanelLayout.PanelState?
        ) {
            if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                binding.toggleBtn.visibility = INVISIBLE
            } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                binding.toggleBtn.visibility = VISIBLE
            }
        }
    }
}