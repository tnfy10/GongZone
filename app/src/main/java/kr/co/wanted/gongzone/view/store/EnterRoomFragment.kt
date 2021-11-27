package kr.co.wanted.gongzone.view.store

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.FragmentEnterRoomBinding
import kr.co.wanted.gongzone.service.SpaceService
import kr.co.wanted.gongzone.utils.PreferenceManager
import kr.co.wanted.gongzone.view.main.MainActivity
import kr.co.wanted.gongzone.view.main.NearMeFragment
import kr.co.wanted.gongzone.viewmodel.StoreViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EnterRoomFragment : Fragment() {

    private lateinit var binding: FragmentEnterRoomBinding
    private lateinit var storeActivity: StoreActivity
    private lateinit var viewModel: StoreViewModel
    private lateinit var seatSelectFragment: SeatSelectFragment
    private lateinit var voucherSelectFragment: VoucherSelectFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeActivity = activity as StoreActivity
        seatSelectFragment = SeatSelectFragment.newInstance()
        voucherSelectFragment = VoucherSelectFragment.newInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterRoomBinding.inflate(inflater, container, false)

        childFragmentManager.beginTransaction().apply {
            replace(binding.tabView.id, seatSelectFragment)
            commit()
        }

        binding.backBtn.setOnClickListener {
            storeActivity.onBackPressed()
        }

        deactivateNextBtn()

        binding.nextBtn.setOnClickListener {
            when (childFragmentManager.findFragmentById(binding.tabView.id)) {
                is SeatSelectFragment -> {
                    childFragmentManager.beginTransaction().apply {
                        replace(binding.tabView.id, voucherSelectFragment)
                        addToBackStack(null)
                        commit()
                    }
                    deactivateEnterRoomBtn()
                }
                is VoucherSelectFragment -> {
                    onClickEnterRoom()
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(storeActivity)[StoreViewModel::class.java]
        viewModel.getSpaceLiveData().observe(viewLifecycleOwner, { spaceItem ->
            binding.storeNameTxt.text = spaceItem.name
        })

        val userNum = NearMeFragment.userNum
        if (userNum != null) {
            viewModel.setUserVoucherListLiveData(userNum)
        }
    }

    private fun onClickEnterRoom() {
        val seatNum = viewModel.getSeatItem().seatNum
        val userNum = NearMeFragment.userNum.toString()
        val voucher = viewModel.getVoucher()
        val spaceNum = viewModel.getSeatItem().spaceNum
        val type = viewModel.getSeatItem().type

        SpaceService.create().enterRoom(seatNum, userNum, voucher.voucherNum, spaceNum, type)
            .enqueue(object: Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    val result = response.body()?.string()

                    if (response.isSuccessful) {
                        when (result) {
                            "1" -> {
                                val nowDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                PreferenceManager.setString(storeActivity, "enterRoomDateTime", nowDateTime)
                                PreferenceManager.setString(storeActivity, "availableTime", voucher.availableTime)
                                showEnterRoomSuccessDialog()
                            }
                            else -> {
                                Toast.makeText(storeActivity, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(storeActivity, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("입실", "통신실패: ${t.message}")
                }
            })
    }

    fun showEnterRoomSuccessDialog() {
        val dialog = Dialog(storeActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_success)

        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val closeBtn = dialog.findViewById<ImageButton>(R.id.closeBtn)
        closeBtn.setOnClickListener {
            dialog.dismiss()
            storeActivity.finish()
        }

        val popUpTxt = dialog.findViewById<TextView>(R.id.popUpTxt)
        popUpTxt.text = "입실이 완료되었습니다!"
    }

    fun activateNextBtn() {
        binding.nextBtn.setImageResource(R.drawable.ic_next_btn_activate)
        binding.nextBtn.isClickable = true
    }

    fun deactivateNextBtn() {
        binding.nextBtn.setImageResource(R.drawable.ic_next_btn_deactivate)
        binding.nextBtn.isClickable = false
    }

    fun activateEnterRoomBtn() {
        binding.nextBtn.setImageResource(R.drawable.ic_enter_room_btn_activate)
        binding.nextBtn.isClickable = true
    }

    fun deactivateEnterRoomBtn() {
        binding.nextBtn.setImageResource(R.drawable.ic_enter_room_btn_deactivate)
        binding.nextBtn.isClickable = false
    }

    companion object {
        fun newInstance(): EnterRoomFragment {
            return EnterRoomFragment()
        }
    }
}