package kr.co.wanted.gongzone.view.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.wanted.gongzone.databinding.FragmentFacilitiesAndRentalBinding

class FacilitiesAndRentalFragment : Fragment() {

    private lateinit var binding: FragmentFacilitiesAndRentalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFacilitiesAndRentalBinding.inflate(inflater, container, false)



        return binding.root
    }

    companion object {
        fun newInstance(): FacilitiesAndRentalFragment{
            return FacilitiesAndRentalFragment()
        }
    }
}