package kr.co.wanted.gongzone.view.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.adapter.VoucherAdapter
import kr.co.wanted.gongzone.databinding.ActivityPurchaseBinding
import kr.co.wanted.gongzone.view.store.SeatSelectFragment
import kr.co.wanted.gongzone.view.store.VoucherSelectFragment
import kr.co.wanted.gongzone.viewmodel.StoreViewModel
import kr.co.wanted.gongzone.viewmodel.VoucherViewModel

class PurchaseActivity : AppCompatActivity() {

    lateinit var binding: ActivityPurchaseBinding
    private lateinit var viewModel: VoucherViewModel
    private lateinit var productSelectFragment: ProductSelectFragment
    lateinit var paymentFragment: PaymentFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(VoucherViewModel::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.setVoucherList(applicationContext)
        }

        productSelectFragment = ProductSelectFragment.newInstance()
        paymentFragment = PaymentFragment.newInstance()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, productSelectFragment)
            commit()
        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.container)) {
            is ProductSelectFragment -> {
                VoucherAdapter().clearCheckedItemList()
                super.onBackPressed()
            }
            else -> super.onBackPressed()
        }
    }
}