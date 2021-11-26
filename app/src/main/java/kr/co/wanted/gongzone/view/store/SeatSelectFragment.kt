package kr.co.wanted.gongzone.view.store

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.FragmentSeatSelectBinding
import kr.co.wanted.gongzone.model.seat.Seat
import kr.co.wanted.gongzone.model.seat.SeatItem
import kr.co.wanted.gongzone.utils.Size
import kr.co.wanted.gongzone.viewmodel.StoreViewModel

class SeatSelectFragment : Fragment() {

    private lateinit var binding: FragmentSeatSelectBinding
    private lateinit var storeActivity: StoreActivity
    private lateinit var viewModel: StoreViewModel
    private lateinit var enterRoomFragment: EnterRoomFragment
    private val alphabet = arrayOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
        "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
        "W", "X", "Y", "Z")
    private var selectedSeatId: Int = -1

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(storeActivity)[StoreViewModel::class.java]
        viewModel.getSeatLiveData().observe(viewLifecycleOwner, { seat ->
            setSeatTable(seat, "S")
            setSeatTable(seat, "M")
            setSeatTable(seat, "L")
        })
    }

    private fun setSeatTable(seat: Seat, type: String) {
        val seatList = getSeatList(seat, type)
        val rowNum = getNumOfTableRow(seatList.size)
        val tableRowArr = Array(rowNum) { TableRow(context) }

        var start = 0
        var end = if (6 < seatList.size) {
            6
        } else {
            seatList.size
        }

        val params = TableRow.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        params.marginEnd = Size.dpToPx(storeActivity, 10f).toInt()

        for (i in 0 until rowNum) {
            while (start < end) {
                val seatBtn = getSeat(seatList[start].seatLocate.toInt(), seatList[start].occupied)
                seatBtn.id = seatList[start].seatNum.toInt()
                seatBtn.setOnClickListener {
                    if (selectedSeatId != -1) {
                        val tmpView = storeActivity.findViewById(selectedSeatId) as TextView
                        tmpView.id = selectedSeatId
                        tmpView.setBackgroundResource(R.drawable.seat_select_positive)
                        tmpView.setTextColor(Color.BLACK)
                    }

                    seatBtn.setBackgroundResource(R.drawable.seat_selected)
                    seatBtn.setTextColor(Color.WHITE)
                    selectedSeatId = seatBtn.id
                    viewModel.setSeatItem(selectedSeatId)
                    enterRoomFragment.activateNextBtn()
                }
                tableRowArr[i].addView(seatBtn, params)
                start++
            }
            tableRowArr[i].setPadding(0, 0, 0, Size.dpToPx(context, 10f).toInt())

            when (type) {
                "S" -> binding.smallTable.addView(tableRowArr[i])
                "M" -> binding.mediumTable.addView(tableRowArr[i])
                "L" -> binding.largeTable.addView(tableRowArr[i])
            }

            if (6 < seatList.size) {
                start = end
                end = start + 1
            } else {
                break
            }
        }
    }

    private fun getNumOfTableRow(size: Int): Int {
        return if (size % 6 != 0) {
            (size / 6) + 1
        } else {
            (size / 6)
        }
    }

    private fun getSeatList(seat: Seat, type: String): ArrayList<SeatItem> {
        val seatList = ArrayList<SeatItem>()

        for (item in seat) {
            when (item.type) {
                type -> {
                    seatList.add(item)
                }
                type -> {
                    seatList.add(item)
                }
                type -> {
                    seatList.add(item)
                }
            }
        }

        return seatList
    }

    private fun getSeat(seatLocate: Int, occupied: String): TextView {
        val textView = TextView(storeActivity)

        textView.gravity = Gravity.CENTER
        textView.text = alphabet[seatLocate]
        textView.textSize = 14f

        when (occupied) {
            "0" -> {
                textView.setBackgroundResource(R.drawable.seat_select_positive)
                textView.isClickable = true
                textView.setTextColor(resources.getColor(R.color.gray_1000, null))
            }
            "1" -> {
                textView.setBackgroundResource(R.drawable.seat_select_negative)
                textView.isClickable = false
                textView.setTextColor(Color.WHITE)
            }
        }

        return textView
    }

    companion object {
        fun newInstance(): SeatSelectFragment {
            return SeatSelectFragment()
        }
    }
}