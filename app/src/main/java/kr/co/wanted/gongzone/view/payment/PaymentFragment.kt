package kr.co.wanted.gongzone.view.payment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.iamport.sdk.data.sdk.IamPortRequest
import com.iamport.sdk.data.sdk.PG
import com.iamport.sdk.data.sdk.PayMethod
import com.iamport.sdk.domain.core.Iamport
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.FragmentPaymentBinding
import kr.co.wanted.gongzone.model.user.User
import kr.co.wanted.gongzone.model.user.UserItem
import kr.co.wanted.gongzone.room.voucher.Voucher
import kr.co.wanted.gongzone.service.SpaceService
import kr.co.wanted.gongzone.service.UserService
import kr.co.wanted.gongzone.utils.PreferenceManager
import kr.co.wanted.gongzone.view.main.NearMeFragment
import kr.co.wanted.gongzone.viewmodel.VoucherViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding
    private lateinit var purchaseActivity: PurchaseActivity
    private lateinit var viewModel: VoucherViewModel
    private lateinit var user: UserItem
    private var userPoint = 0
    private var productAmount = 0
    private var finalPaymentAmount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        purchaseActivity = activity as PurchaseActivity
        Iamport.init(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)

        getUserInfo()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(purchaseActivity)[VoucherViewModel::class.java]
        val voucherList = viewModel.getSelectedVoucherList().value as ArrayList<Voucher>

        val voucherName = "${voucherList[0].kind} 이용권 - ${voucherList[0].name}"
        binding.voucherNameTxt.text = voucherName

        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        val endDate = LocalDate.now().plusYears(1L).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

        "${currentDate}~${endDate}".also { binding.availablePeriodTxt.text = it }
        "${voucherList[0].time} 시간".also { binding.availableTimeTxt.text = it }

        productAmount = voucherList[0].price
        "${DecimalFormat("###,###,###").format(productAmount)} 원".also {
            binding.productAmountTxt.text = it }

        finalPaymentAmount = productAmount
        "${DecimalFormat("###,###,###").format(finalPaymentAmount)} 원".also {
            binding.finalPaymentAmountTxt.text = it }

        binding.pointEdt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val point = if (binding.pointEdt.text.toString().isEmpty()) {
                    0
                } else {
                    binding.pointEdt.text.toString().toInt()
                }

                if (userPoint < point) {
                    binding.pointEdt.setText(userPoint.toString())
                    binding.pointEdt.setSelection(binding.pointEdt.text.length)
                    finalPaymentAmount = productAmount - userPoint
                    "-${DecimalFormat("###,###,###").format(userPoint)} 원".also {
                        binding.discountAmountTxt.text = it }
                } else {
                    finalPaymentAmount = productAmount - point
                    "-${DecimalFormat("###,###,###").format(point)} 원".also {
                        binding.discountAmountTxt.text = it }
                }

                "${DecimalFormat("###,###,###").format(finalPaymentAmount)} 원".also {
                    binding.finalPaymentAmountTxt.text = it }
            }

            override fun afterTextChanged(s: Editable?) { }

        })

        val userCode = "imp66157918"  // 가맹점식별코드, "iamport" 는 테스트용 코드임
        // SDK 에 결제 요청할 데이터
        val request = IamPortRequest(
            pg = PG.html5_inicis.makePgRawName("inicis"),         // PG사
            pay_method = PayMethod.card.name,                    // 결제수단
            name = voucherName,                      // 주문명
            merchant_uid = "sample_aos_${Date().time}",     // 주문번호
            amount = "10",                                // 결제금액
            buyer_name = "테스트"  // TODO: 사용자 이름 들어가게끔 변경
        )

        binding.paymentBtn.setOnClickListener {
            Iamport.payment(userCode, iamPortRequest = request, paymentResultCallback = {
                if (it != null) {
                    if (it.imp_success == true) {
                        val userNum = NearMeFragment.userNum.toString()
                        val availableTime = voucherList[0].time.toString()
                        val day = voucherList[0].day.toString()

                        when (voucherList[0].kind) {
                            "1회" -> {
                                paymentInfoInsert(userNum, "once", availableTime, day)
                            }
                            "시간제" -> {
                                paymentInfoInsert(userNum, "time", availableTime, day)
                            }
                            "기간제" -> {
                                paymentInfoInsert(userNum, "period", availableTime, day)
                            }
                        }
                    } else {
                        val msg = "에러코드: ${it.error_code}\n에러메시지: ${it.error_msg}"
                        Toast.makeText(purchaseActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }

    /**
     * 이용권 구매 완료 팝업
     */
    private fun showPaymentSuccessDialog() {
        val dialog = Dialog(purchaseActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_success)

        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val closeBtn = dialog.findViewById<ImageButton>(R.id.closeBtn)
        closeBtn.setOnClickListener {
            dialog.dismiss()
            purchaseActivity.finish()
        }

        val popUpTxt = dialog.findViewById<TextView>(R.id.popUpTxt)
        popUpTxt.text = "이용권 구매가 완료되었습니다!"
    }

    /**
     * 결제 정보 등록
     */
    private fun paymentInfoInsert(userNum: String, type: String, availableTime: String, day: String) {
        SpaceService.create().addVoucher(userNum, type, availableTime, day).enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    when (response.body()?.string()) {
                        "1" -> {
                            Iamport.close()
                            addPaymentHistory(userNum, type, availableTime, day)
                            showPaymentSuccessDialog()
                        }
                        else -> {
                            Toast.makeText(purchaseActivity, "결제 정보 등록 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(purchaseActivity, "결제 정보 등록 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("결제 정보 등록", "통신실패: ${t.message}")
            }

        })
    }

    private fun addPaymentHistory(userNum: String, type: String, availableTime: String, day: String) {
        SpaceService.create().paymentVoucher(
            productAmount.toString(),
            userPoint.toString(),
            userNum,
            type,
            availableTime,
            day).enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("결제 정보 등록", "응답코드: ${response.body()?.string()}")
                } else {
                    Log.d("결제 정보 등록", "응답 받지 못함")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("결제 정보 등록", "통신실패: ${t.message}")
            }

        })
    }

    /**
     * 사용자 정보 로드
     */
    private fun getUserInfo() {
        val id = PreferenceManager.getString(purchaseActivity, "userId")
        val pwd = PreferenceManager.getString(purchaseActivity, "pwd")

        if (id!=null && pwd!=null) {
            UserService.create().getUser(id, pwd).enqueue(object: Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val tmp = response.body()?.get(0)

                    if (tmp!=null) {
                        user = tmp
                        userPoint = tmp.point.toInt()
                        "${DecimalFormat("###,###,###").format(userPoint)} P 사용 가능".also {
                            binding.availablePointTxt.text = it }
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("PaymentFragment", "통신실패: ${t.message}")
                }

            })
        }
    }

    companion object {
        fun newInstance(): PaymentFragment {
            return PaymentFragment()
        }
    }
}