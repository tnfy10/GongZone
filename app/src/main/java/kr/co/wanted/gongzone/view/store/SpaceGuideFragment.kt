package kr.co.wanted.gongzone.view.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.wanted.gongzone.databinding.FragmentSpaceGuideBinding

class SpaceGuideFragment : Fragment() {

    private lateinit var binding: FragmentSpaceGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpaceGuideBinding.inflate(inflater, container, false)



        return binding.root
    }

    companion object {
        fun newInstance(): SpaceGuideFragment{
            return SpaceGuideFragment()
        }
    }
}