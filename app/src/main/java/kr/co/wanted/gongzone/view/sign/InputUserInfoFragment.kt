package kr.co.wanted.gongzone.view.sign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.wanted.gongzone.databinding.FragmentInputUserInfoBinding

class InputUserInfoFragment : Fragment() {

    private lateinit var binding: FragmentInputUserInfoBinding
    private lateinit var signUpActivity: SignUpActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpActivity = activity as SignUpActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInputUserInfoBinding.inflate(inflater, container, false)

        signUpActivity.binding.backBtn.setOnClickListener {
            signUpActivity.onBackPressed()
        }

        binding.signUpBtn.setOnClickListener {
            onClickSignUpBtn()
        }

        return binding.root
    }

    private fun onClickSignUpBtn() {
        signUpActivity.finish()
    }

    companion object {
        fun newInstance(): InputUserInfoFragment {
            return InputUserInfoFragment()
        }
    }
}