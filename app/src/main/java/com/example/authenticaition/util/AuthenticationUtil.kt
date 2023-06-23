package com.example.authenticaition.util

import android.app.Activity
import android.app.KeyguardManager
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import com.example.authenticaition.R

class AuthenticationUtil(private val activity: Activity) {

    companion object {
        const val LOCK_REQUEST_CODE = 101
        const val SECURITY_SETTING_REQUEST_CODE = 102
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LOCK_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    // If screen lock authentication is successful, update text
                    showLog("Auth success")
                } else {
                    // If screen lock authentication fails, finish the activity
                    activity.finishAffinity()
                }
            }
            SECURITY_SETTING_REQUEST_CODE -> {
                // When user enables Security settings, we don't get any RESULT_OK
                // So we need to check whether device has enabled screen lock or not
                if (isDeviceSecure()) {
                    // If screen lock is enabled, show a toast and start intent to authenticate the user
                    showToast("Device is secure")
                    authenticateApp()
                } else {
                    // If screen lock is not enabled, just update text
                    showToast("Security device cancelled")
                }
            }
        }
    }

    private fun isDeviceSecure(): Boolean {
        val keyguardManager = activity.getSystemService(Activity.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.isKeyguardSecure
    }

    private fun authenticateApp() {
        val keyguardManager = activity.getSystemService(Activity.KEYGUARD_SERVICE) as KeyguardManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val intent = keyguardManager.createConfirmDeviceCredentialIntent(
                activity.resources.getString(R.string.unlock),
                activity.resources.getString(R.string.confirm_pattern)
            )
            try {
                activity.startActivityForResult(intent, LOCK_REQUEST_CODE)
            } catch (e: Exception) {
                val securitySettingsIntent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                try {
                    activity.startActivityForResult(securitySettingsIntent, SECURITY_SETTING_REQUEST_CODE)
                } catch (ex: Exception) {
                    showToast("Security device cancelled")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLog(message: String) {
        // Your implementation for logging goes here
    }
}
