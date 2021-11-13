package kr.co.wanted.gongzone.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.idEdt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.idTxt.setTextColor(resources.getColor(R.color.main_color, null))
            } else {
                binding.idTxt.setTextColor(resources.getColor(R.color.gray_text, null))
            }
        }

        binding.pwdEdt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.pwdTxt.setTextColor(resources.getColor(R.color.main_color, null))
            } else {
                binding.pwdTxt.setTextColor(resources.getColor(R.color.gray_text, null))
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
            Toast.makeText(applicationContext, "회원가입", Toast.LENGTH_SHORT).show()
        }

        binding.findIdBtn.setOnClickListener {
            Toast.makeText(applicationContext, "아이디 찾기", Toast.LENGTH_SHORT).show()
        }

        binding.findPwdBtn.setOnClickListener {
            Toast.makeText(applicationContext, "비밀번호 찾기", Toast.LENGTH_SHORT).show()
        }
    }
}