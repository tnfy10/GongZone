package kr.co.wanted.gongzone.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.wanted.gongzone.databinding.FragmentUseStatusBinding

class UseStatusFragment : Fragment() {

    private lateinit var binding: FragmentUseStatusBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUseStatusBinding.inflate(inflater, container, false)



        return binding.root
    }

    companion object {
        fun newInstance() : UseStatusFragment {
            return UseStatusFragment()
        }
    }
}