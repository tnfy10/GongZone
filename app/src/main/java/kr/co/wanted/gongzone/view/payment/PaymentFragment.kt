package kr.co.wanted.gongzone.view.payment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kr.co.wanted.gongzone.service.UserService
import kr.co.wanted.gongzone.utils.PreferenceManager
import kr.co.wanted.gongzone.view.main.NearMeFragment
import kr.co.wanted.gongzone.viewmodel.VoucherViewModel
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
    private var finalPaymentAmount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        purchaseActivity = activity as PurchaseActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)

        getUserInfo()

        binding.paymentBtn.setOnClickListener {
            Toast.makeText(context, finalPaymentAmount.toString(), Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(purchaseActivity)[VoucherViewModel::class.java]
        val voucherList = viewModel.getSelectedVoucherList().value as ArrayList<Voucher>
        "${voucherList[0].kind} 이용권 - ${voucherList[0].name}".also { binding.voucherNameTxt.text = it }

        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        val endDate = LocalDate.now().plusYears(1L).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

        "${currentDate}~${endDate}".also { binding.availablePeriodTxt.text = it }
        "${voucherList[0].time} 시간".also { binding.availableTimeTxt.text = it }

        val productAmount = voucherList[0].price
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