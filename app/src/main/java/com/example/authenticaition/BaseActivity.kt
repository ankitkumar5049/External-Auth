package com.example.authenticaition


import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.authenticaition.util.AuthenticationUtil.Companion.LOCK_REQUEST_CODE
import com.example.authenticaition.util.AuthenticationUtil.Companion.SECURITY_SETTING_REQUEST_CODE
import com.google.android.material.snackbar.Snackbar


open class BaseActivity: AppCompatActivity() {

    private var locked = false
    private lateinit var dialog: Dialog


    private val LOCK_REQUEST_CODE = 101
    private val SECURITY_SETTING_REQUEST_CODE = 102

    private val lockDelayMillis: Long = 1 * 5 * 1000 // 2 minutes in milliseconds

    private var lastInteractionTime: Long = 0
    private val lockHandler = Handler(Looper.getMainLooper())
    private val lockRunnable = Runnable { lockApp() }
    private var isInBackground: Boolean = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//            setupTouchListeners()

        // Set up user interaction listener
        setInteractionListener()

        // Start the lock timer when the app is launched
        startLockTimer()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }


    override fun onResume() {
        super.onResume()
        isInBackground = false
        resetLockTimer()
    }

    override fun onPause() {
        super.onPause()
        isInBackground = true
        resetLockTimer()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        resetLockTimer()
    }

    private fun setInteractionListener() {
        val rootView = findViewById<View>(android.R.id.content)
        rootView.setOnTouchListener { _, _ ->
            resetLockTimer()
            false
        }
    }

    private fun startLockTimer() {
        lastInteractionTime = System.currentTimeMillis()
        lockHandler.postDelayed(lockRunnable, lockDelayMillis)
    }

    private fun stopLockTimer() {
        lockHandler.removeCallbacks(lockRunnable)
    }

    private fun resetLockTimer() {
        stopLockTimer()
//        if (!isInBackground) {
            startLockTimer()
//        }
    }

    private fun lockApp() {
        if (isDeviceSecure()) {
            authenticateApp()
        } else {
            showToast(getString(R.string.security_device_cancelled))
        }
    }

    private fun isDeviceSecure(): Boolean {
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && keyguardManager.isKeyguardSecure
    }

     fun authenticateApp() {
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val i = keyguardManager.createConfirmDeviceCredentialIntent(
                resources.getString(R.string.unlock),
                resources.getString(R.string.confirm_pattern)
            )
            try {
                startActivityForResult(i, LOCK_REQUEST_CODE)
            } catch (e: Exception) {
                val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                try {
                    startActivityForResult(intent, SECURITY_SETTING_REQUEST_CODE)
                } catch (ex: Exception) {
                    showToast(getString(R.string.security_device_cancelled))
                }
            }
        }
    }

    companion object {
        private const val LOCK_REQUEST_CODE = 1
        private const val SECURITY_SETTING_REQUEST_CODE = 2
    }

    fun showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()


    }
    fun showSnackbar(view: View,message: String){
//        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()

        Snackbar.make(this,view,message,Snackbar.LENGTH_SHORT).show()
    }



    fun showLog(message:String){
        Log.i("ankit", "showLog: $message")
    }


    fun showDebugLog(tag: String, message: String){
        Log.d(tag,"customLog: $message")
    }

}