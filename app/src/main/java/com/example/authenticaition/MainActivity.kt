package com.example.authenticaition

import android.app.KeyguardManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.biometric.BiometricPrompt
import com.example.authenticaition.util.AuthenticationUtil

class MainActivity : BaseActivity() {
    private val LOCK_REQUEST_CODE = 221
    private val SECURITY_SETTING_REQUEST_CODE = 233
    private var textView: TextView? = null


    private lateinit var inactivityLock: AppLockManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView =  findViewById(R.id.texview);
        inactivityLock = AppLockManager(this)

//        authenticateApp()

    }

    fun authenticateAgain(view: View) {
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            LOCK_REQUEST_CODE -> if (resultCode === RESULT_OK) {
                //If screen lock authentication is success update text
                isAppLocked = false
                showLog("Auth success")
            } else {
                //If screen lock authentication is failed
//                finishAffinity()
            }

            SECURITY_SETTING_REQUEST_CODE ->
                //When user is enabled Security settings then we don't get any kind of RESULT_OK
                //So we need to check whether device has enabled screen lock or not
                if (isDeviceSecure()) {
                    //If screen lock enabled show toast and start intent to authenticate user
                    showToast(getString(R.string.device_is_secure))
                    authenticateApp()
                } else {
                    //If screen lock is not enabled just update text
                    showToast(getString(R.string.security_device_cancelled))
                }
        }
    }

    private fun isDeviceSecure(): Boolean {
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager

        //this method only work whose api level is greater than or equal to Jelly_Bean (16)
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && keyguardManager.isKeyguardSecure
        //You can also use keyguardManager.isDeviceSecure(); but it requires API Level 23
    }


    private fun authenticateApp() {
        //Get the instance of KeyGuardManager
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager

        //Check if the device version is greater than or equal to Lollipop(21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Create an intent to open device screen lock screen to authenticate
            //Pass the Screen Lock screen Title and Description
            val i = keyguardManager.createConfirmDeviceCredentialIntent(
                resources.getString(R.string.unlock),
                resources.getString(R.string.confirm_pattern)
            )
            try {
                //Start activity for result
                startActivityForResult(i, LOCK_REQUEST_CODE)
            } catch (e: java.lang.Exception) {

                //If some exception occurs means Screen lock is not set up please set screen lock
                //Open Security screen directly to enable patter lock
                val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                try {

                    //Start activity for result
                    startActivityForResult(intent, SECURITY_SETTING_REQUEST_CODE)
                } catch (ex: java.lang.Exception) {

                    //If app is unable to find any Security settings then user has to set screen lock manually
                    showToast(getString(R.string.security_device_cancelled))
                }
            }
        }
    }

    override fun onBackPressed() {
        if(!isAppLocked){ super.onBackPressed() }
        else{finishAffinity()}
    }

}