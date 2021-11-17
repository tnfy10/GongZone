package kr.co.wanted.gongzone.view.sign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.wanted.gongzone.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}