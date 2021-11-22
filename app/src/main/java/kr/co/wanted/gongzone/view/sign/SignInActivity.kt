package kr.co.wanted.gongzone.view.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.ActivitySignInBinding
import kr.co.wanted.gongzone.model.user.User
import kr.co.wanted.gongzone.service.UserService
import kr.co.wanted.gongzone.utils.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            val userId = binding.idEdt.text.toString().trim().replace(" ", "")
            val pwd = binding.pwdEdt.text.toString().trim().replace(" ", "")

            if (userId.isEmpty() || pwd.isEmpty()) {
                // TODO: 입력해주세요 알림 띄우기
            } else {
                signIn(userId, pwd)
            }
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

    /**
     * 로그인
     */
    private fun signIn(userId: String, pwd: String) {
        UserService.create().getUser(userId, pwd).enqueue(object: Callback<User>{
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()?.get(0)

                    if (user != null) {
                        PreferenceManager.setString(applicationContext, "userId", userId)
                        PreferenceManager.setString(applicationContext, "pwd", pwd)
                        PreferenceManager.setBoolean(applicationContext, "isSigned", true)
                        finish()
                    } else {
                        Log.d(TAG, "사용자 정보 없음")
                    }

                } else {
                    Log.d(TAG, response.message())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d(TAG, "통신실패: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG= "SignInActivity"
    }
}