package kr.co.wanted.gongzone.view.sign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.ActivitySignUpBinding
import kr.co.wanted.gongzone.view.main.NearMeFragment

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    lateinit var termsAndConditionFragment: TermsAndConditionFragment
    lateinit var inputUserInfoFragment: InputUserInfoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        termsAndConditionFragment = TermsAndConditionFragment.newInstance()
        inputUserInfoFragment = InputUserInfoFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, termsAndConditionFragment)
            .commit()
    }
}