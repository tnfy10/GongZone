package kr.co.wanted.gongzone.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.FragmentMyBinding
import kr.co.wanted.gongzone.databinding.FragmentNearMeBinding

class MyFragment : Fragment() {

    private lateinit var binding: FragmentMyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBinding.inflate(inflater, container, false)



        return binding.root
    }

    companion object {
        fun newInstance() : MyFragment {
            return MyFragment()
        }
    }
}