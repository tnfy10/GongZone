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
    private val chips = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        basicChips = binding.basicChipGroup
        facilityChips = binding.facilityChipGroup
        tableTypeChips = binding.tableTypeChipGroup
        rentableItemChips = binding.rentableItemChipGroup

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
            addSelectedChipsText(basicChips)
            addSelectedChipsText(facilityChips)
            addSelectedChipsText(tableTypeChips)
            addSelectedChipsText(rentableItemChips)

            val intent = Intent()
            intent.putStringArrayListExtra("selectedChips", chips)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }

    private fun addSelectedChipsText(chipGroup: ChipGroup) {
        for (i in 0 until chipGroup.childCount) {
            chips.add((chipGroup.getChildAt(i) as Chip).text as String)
        }
    }
}