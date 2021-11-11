package kr.co.wanted.gongzone.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 100
    }

    private lateinit var binding: ActivityLoginBinding

    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    } else {
        arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.all { permissions -> permissions.value == true }) {
            Toast.makeText(this, "권한 허용됨1", Toast.LENGTH_SHORT).show()
            setTheme(R.style.Theme_GongZone)
        } else {
            Toast.makeText(this, "권한 거부됨1", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (checkPermission(permissions)) {
            requestPermissions(permissions, REQUEST_CODE_PERMISSIONS)
        }

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.testBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    /**
     * 권한 체크
     */
    private fun checkPermission(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED
        }
    }

    /**
     * 권한 요청 결과
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSIONS -> {
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    Toast.makeText(this, "권한 허용됨2", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "권한 거부됨2", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}