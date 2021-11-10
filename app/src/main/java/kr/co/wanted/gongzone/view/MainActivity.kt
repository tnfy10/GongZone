package kr.co.wanted.gongzone.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.WindowManager
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kr.co.wanted.gongzone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        setContentView(binding.root)

        val slidePanel = binding.mainFrame
        slidePanel.addPanelSlideListener(PanelEventListener())

        // 패널 열고 닫기
        /*
        binding.toggleBtn.setOnClickListener {
            val state = slidePanel.panelState

            // 닫힌 상태일 경우 열기
            if (state == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                slidePanel.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
            }
            // 열린 상태일 경우 닫기
            else if (state == SlidingUpPanelLayout.PanelState.EXPANDED) {
                slidePanel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            }
        }
         */
    }

    inner class PanelEventListener : SlidingUpPanelLayout.PanelSlideListener {
        // 패널이 슬라이드 중일 때
        override fun onPanelSlide(panel: View?, slideOffset: Float) {
            // TODO
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