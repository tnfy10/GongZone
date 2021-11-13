package kr.co.wanted.gongzone.view

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kr.co.wanted.gongzone.R
import kr.co.wanted.gongzone.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION)

    private val permissionResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.all { permission -> permission.value == true }) {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            } else {
                Log.d("PermissionCheck", "권한없음 - registerForActivityResult")
                showPermissionGuideDialog()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()

        if (checkPermissions(permissions)) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        } else {
            Log.d("PermissionCheck", "권한없음 - checkPermissions")
        }
    }

    /**
     * 권한 검사
     */
    private fun checkPermissions(permissions: Array<String>): Boolean {
        val preference = getPreferences(Context.MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true)
        return if (permissions.all { ContextCompat.checkSelfPermission(applicationContext, it) != PackageManager.PERMISSION_GRANTED }) {
            if (permissions.all { ActivityCompat.shouldShowRequestPermissionRationale(this, it) }) {
                permissionResultLauncher.launch(permissions)
            } else {
                if (isFirstCheck) {
                    preference.edit().putBoolean("isFirstPermissionCheck", false).apply()
                    permissionResultLauncher.launch(permissions)
                } else {
                    showPermissionGuideDialog()
                }
            }
            false
        } else {
            true
        }
    }

    /**
     * 권한 안내 다이얼로그
     */
    private fun showPermissionGuideDialog() {
        val builder = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle("권한 안내")
            .setMessage("앱을 사용하기 위해서는 위치 권한이 필요합니다.")
            .setPositiveButton("설정") { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("닫기") { _, _ ->
                finish()
            }
        builder.show()
    }
}