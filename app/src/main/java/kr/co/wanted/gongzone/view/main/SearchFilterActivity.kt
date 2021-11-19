package kr.co.wanted.gongzone.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.ActivitySearchFilterBinding
import okhttp3.internal.checkDuration
import java.util.ArrayList

class SearchFilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchFilterBinding
    private lateinit var basicChips: ChipGroup
    private lateinit var facilityChips: ChipGroup
    private lateinit var tableTypeChips: ChipGroup
    private lateinit var rentableItemChips: ChipGroup
    private val chipIdList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        basicChips = binding.basicChipGroup
        facilityChips = binding.facilityChipGroup
        tableTypeChips = binding.tableTypeChipGroup
        rentableItemChips = binding.rentableItemChipGroup

        setCheckChips()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.resetBtn.setOnClickListener {
            binding.basicChipGroup.clearCheck()
            binding.facilityChipGroup.clearCheck()
            binding.tableTypeChipGroup.clearCheck()
            binding.rentableItemChipGroup.clearCheck()
        }

        binding.applyBtn.setOnClickListener {
            addSelectedChipIds(basicChips.checkedChipIds)
            addSelectedChipIds(facilityChips.checkedChipIds)
            addSelectedChipIds(tableTypeChips.checkedChipIds)
            addSelectedChipIds(rentableItemChips.checkedChipIds)

            val data = Intent()
            data.putIntegerArrayListExtra("chipIdList", chipIdList)
            setResult(RESULT_OK, data)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }

    /**
     * 기존 설정된 필터가 있으면 체크
     */
    private fun setCheckChips() {
        val chipsIdList = intent.getIntegerArrayListExtra("chipIdList")
        if (chipsIdList != null) {
            for (id in chipsIdList) {
                basicChips.check(id)
                facilityChips.check(id)
                tableTypeChips.check(id)
                rentableItemChips.check(id)
            }
        }
    }

    /**
     * 체크된 필터 id를 리스트에 추가
     */
    private fun addSelectedChipIds(idList: MutableList<Int>) {
        for (id in idList) {
            chipIdList.add(id)
        }
    }
}