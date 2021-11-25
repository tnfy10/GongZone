package kr.co.wanted.gongzone.view.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.FragmentFacilitiesAndRentalBinding
import kr.co.wanted.gongzone.model.space.SpaceItem
import kr.co.wanted.gongzone.utils.Size
import kr.co.wanted.gongzone.viewmodel.StoreViewModel

class FacilitiesAndRentalFragment : Fragment() {

    private lateinit var binding: FragmentFacilitiesAndRentalBinding
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
        binding = FragmentFacilitiesAndRentalBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(storeActivity)[StoreViewModel::class.java]
        viewModel.getSpaceLiveData().observe(viewLifecycleOwner, { spaceItem ->
            showFacilityTable(spaceItem)
            showRentableTable(spaceItem)
        })
    }

    private fun showFacilityTable(spaceItem: SpaceItem) {
        val facility = spaceItem.facility.split(";;")

        val imgParams = LinearLayout.LayoutParams(Size.dpToPx(context, 0f).toInt(), WRAP_CONTENT)
        imgParams.weight = 1f

        var size = facility.size/4
        if (facility.size % 4 != 0) size++

        val tableRowArr = Array(size) { TableRow(context) }
        binding.facilityTable.addView(tableRowArr[0])

        for (i in 1 until tableRowArr.size) {
            tableRowArr[i].setPadding(0, Size.dpToPx(context, 10f).toInt(), 0, 0)
            binding.facilityTable.addView(tableRowArr[i])
        }

        var j = 0

        for (i in facility.indices) {
            if (i % 4 == 0 && i != 0) j++

            val imageView = ImageView(context)

            when (facility[i]) {
                resources.getString(R.string.square_table) ->
                    imageView.setImageResource(R.drawable.ic_square_table)
                resources.getString(R.string.individual_stand) ->
                    imageView.setImageResource(R.drawable.ic_personal_stand)
                resources.getString(R.string.white_light) ->
                    imageView.setImageResource(R.drawable.ic_white_light)
                resources.getString(R.string.socket) ->
                    imageView.setImageResource(R.drawable.ic_soket)
                resources.getString(R.string.backrest_chair) ->
                    imageView.setImageResource(R.drawable.ic_chair)
                resources.getString(R.string.restroom) ->
                    imageView.setImageResource(R.drawable.ic_lestroom)
            }
            tableRowArr[j].addView(imageView)
        }
    }

    private fun showRentableTable(spaceItem: SpaceItem) {
        val rentable = spaceItem.rent.split(";;")

        val imgParams = LinearLayout.LayoutParams(Size.dpToPx(context, 0f).toInt(), WRAP_CONTENT)
        imgParams.weight = 1f

        var size = rentable.size/4
        if (rentable.size % 4 != 0) size++

        val tableRowArr = Array(size) { TableRow(context) }
        binding.rentableTable.addView(tableRowArr[0])

        for (i in 1 until tableRowArr.size) {
            tableRowArr[i].setPadding(0, Size.dpToPx(context, 10f).toInt(), 0, 0)
            binding.rentableTable.addView(tableRowArr[i])
        }

        var j = 0

        for (i in rentable.indices) {
            if (i % 4 == 0 && i != 0) j++

            val imageView = ImageView(context)

            when (rentable[i]) {
                resources.getString(R.string.charger) ->
                    imageView.setImageResource(R.drawable.ic_charger)
                resources.getString(R.string.reading_prop) ->
                    imageView.setImageResource(R.drawable.ic_reading_stand)
                resources.getString(R.string.blanket) ->
                    imageView.setImageResource(R.drawable.ic_blanket)
            }
            tableRowArr[j].addView(imageView)
        }
    }

    companion object {
        fun newInstance(): FacilitiesAndRentalFragment{
            return FacilitiesAndRentalFragment()
        }
    }
}