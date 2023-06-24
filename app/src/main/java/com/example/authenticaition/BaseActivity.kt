package com.example.authenticaition


import android.app.KeyguardManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.snackbar.Snackbar


open class BaseActivity : AppCompatActivity() {

    companion object {
        private const val LOCK_REQUEST_CODE = 1
        private const val SECURITY_SETTING_REQUEST_CODE = 2
    }

    private val lockDelayMillis: Long = 1 * 5 * 1000 // 2 minutes in milliseconds

    private var lastInteractionTime: Long = 0
    var isAppLocked: Boolean = true
    private val lockHandler = Handler(Looper.getMainLooper())
    private val lockRunnable = Runnable {
        if (!isAppLocked) {
            lockApp()
        }
    }
    private var isInBackground: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up user interaction listener
        setInteractionListener()

        // Start the lock timer when the app is launched
        startLockTimer()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    override fun onBackPressed() {
        if (!isAppLocked) {
            super.onBackPressed()
        } else {
            finishAffinity()
        }
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
            showLog("screen touched")
            false
        }
    }

    private fun startLockTimer() {
        showLog("start timer")
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
            isAppLocked = true
            showLog("lock app")
            authenticateApp()
        } else {
            showToast(getString(R.string.security_device_cancelled))
        }
    }

    private fun isDeviceSecure(): Boolean {
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && keyguardManager.isKeyguardSecure
    }

    private fun authenticateApp() {
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val i = keyguardManager.createConfirmDeviceCredentialIntent(
                resources.getString(R.string.unlock),
                resources.getString(R.string.confirm_pattern)
            )
            try {
                showLog("no exception")
                startActivityForResult(i, LOCK_REQUEST_CODE)
            } catch (e: Exception) {

                // If app is unable to find any Security settings, show a message to the user
                showLog("exception")
                // Lock the app immediately since screen lock is not available
                lockApp()
                val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                try {
                    showLog("open setting")
                    startActivityForResult(intent, SECURITY_SETTING_REQUEST_CODE)
                } catch (ex: Exception) {
                    showToast(getString(R.string.security_device_cancelled))
                }
            }
        }
    }



//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            LOCK_REQUEST_CODE -> if (resultCode === RESULT_OK) {
//                //If screen lock authentication is success update text
//                unlockApp()
//                showLog("Auth success")
//            } else {
//                //If screen lock authentication is failed
//                unlockApp()
//                showLog("close activity")
//                finishAffinity()
//            }
//
//            SECURITY_SETTING_REQUEST_CODE ->
//                //When user is enabled Security settings then we don't get any kind of RESULT_OK
//                //So we need to check whether device has enabled screen lock or not
//                if (isDeviceSecure()) {
//                    showLog("device secure")
//                    //If screen lock enabled show toast and start intent to authenticate user
//                    if (!isAppLocked) {
//                        authenticateApp()
//                    }
//                    unlockApp()
//                    showToast(getString(R.string.device_is_secure))
//                } else {
//                    showLog("not secured")
//                    //If screen lock is not enabled just update text
//                    showToast(getString(R.string.security_device_cancelled))
//                    lockApp()
//                }
//        }
//    }

    private fun unlockApp() {
        isAppLocked = false
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showSnackbar(view: View, message: String) {
//        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()

        Snackbar.make(this, view, message, Snackbar.LENGTH_SHORT).show()
    }


    fun showLog(message: String) {
        Log.i("ankit", "showLog: $message")
    }


    fun showDebugLog(tag: String, message: String) {
        Log.d(tag, "customLog: $message")
    }


}