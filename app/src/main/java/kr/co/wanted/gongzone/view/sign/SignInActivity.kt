package kr.co.wanted.gongzone.view.sign

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.idEdt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.idTxt.setTextColor(resources.getColor(R.color.main_color, null))
            } else {
                binding.idTxt.setTextColor(resources.getColor(R.color.gray_800, null))
            }
        }

        binding.pwdEdt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.pwdTxt.setTextColor(resources.getColor(R.color.main_color, null))
            } else {
                binding.pwdTxt.setTextColor(resources.getColor(R.color.gray_800, null))
            }
        }

        binding.signInBtn.setOnClickListener {
            val intent = Intent()
            intent.putExtra("isSigned", true)
            intent.putExtra("id", "test")
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }

        binding.findIdBtn.setOnClickListener {
            Toast.makeText(applicationContext, "아이디 찾기", Toast.LENGTH_SHORT).show()
        }

        binding.findPwdBtn.setOnClickListener {
            Toast.makeText(applicationContext, "비밀번호 찾기", Toast.LENGTH_SHORT).show()
        }
    }
}