package kr.co.wanted.gongzone.view.sign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.FragmentTermsAndConditionBinding

class TermsAndConditionFragment : Fragment() {

    private lateinit var binding: FragmentTermsAndConditionBinding
    private lateinit var signUpActivity: SignUpActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpActivity = activity as SignUpActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTermsAndConditionBinding.inflate(inflater, container, false)

        signUpActivity.binding.backBtn.setOnClickListener {
            signUpActivity.onBackPressed()
        }

        binding.allAgreeChkBox.setOnCheckedChangeListener { _, isChecked ->
            onClickAllAgreeCheckBox(isChecked)
        }

        binding.nextBtn.setOnClickListener {
            onClickNextButton()
        }

        return binding.root
    }

    private fun onClickAllAgreeCheckBox(isChecked: Boolean) {
        if (isChecked) {
            binding.userTermsAgreeChkBox.isChecked = isChecked
            binding.privacyPolicyAgreeChkBox.isChecked = isChecked
            binding.locationServiceAgreeChkBox.isChecked = isChecked
            binding.receiveMarketingInfoAgreeChkBox.isChecked = isChecked
        } else {
            binding.userTermsAgreeChkBox.isChecked = isChecked
            binding.privacyPolicyAgreeChkBox.isChecked = isChecked
            binding.locationServiceAgreeChkBox.isChecked = isChecked
            binding.receiveMarketingInfoAgreeChkBox.isChecked = isChecked
        }
    }

    private fun onClickNextButton() {
        if (binding.userTermsAgreeChkBox.isChecked
            && binding.privacyPolicyAgreeChkBox.isChecked) {
            signUpActivity
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, signUpActivity.inputUserInfoFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        fun newInstance(): TermsAndConditionFragment {
            return TermsAndConditionFragment()
        }
    }
}