package com.example.authenticaition


import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
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

//    lateinit var commonDialogBox: CommonDialogBox
//    lateinit var commonProgressDialog: CommonProgressDialog
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    var locked = false
    private lateinit var dialog: Dialog

    // Observer used for handling the seconds taken at each activity.

    public var activityStayCounter:MutableLiveData<Int> = MutableLiveData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        commonProgressDialog = CommonProgressDialog(this)

        // code for to avoid the user from taking screenshot or screen-record option.
        try{
//            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        }catch (e:java.lang.Exception){
            e.printStackTrace()
            Log.i("DEV",e.message!!)
        }
        // Setting up the day mode always by default for the application.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    // Common method for activity timer countdown type(5minutes max per activity)

    fun startActivityCompleteTimer():Int{
        var count =0
        // Need to handle lifecycle callbacks of android as well.
        // 70,000 = 70 Seconds Countdown timer.
        // We are setting 5 mins timer for each activity. 300 seconds = 5 mins

        // Tested on 300 seconds printed in logcat. insert 3 three zeros after required seconds.
        val timer = object: CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                count++
                // Problem 2 : this function is running return count to activity onTick()
                showLog("Seconds --> $count")
                // Calling another function solution one.
                if(count>=60){
                    if(count==60){
                        activityStayCounter.postValue(count)

                    }else if(count==120){
                        activityStayCounter.postValue(count)

                    }


                }else if(count==30){
                    activityStayCounter.postValue(count)

                }


            }

            override fun onFinish() {


            }
        }
        timer.start()

        return count


    }

    fun showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()


    }
    fun showSnackbar(view: View,message: String){
//        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()

        Snackbar.make(this,view,message,Snackbar.LENGTH_SHORT).show()
    }



    fun hasReadPhoneStatePermission(context: Context): Boolean {
        val permission = android.Manifest.permission.READ_PHONE_STATE
        val result = ContextCompat.checkSelfPermission(context, permission)
        return result == PackageManager.PERMISSION_GRANTED
    }




    fun showLog(message:String){
        Log.i("ankit", "showLog: $message")
    }


    fun showDebugLog(tag: String, message: String){
        Log.d(tag,"customLog: $message")
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            LOCK_REQUEST_CODE -> if (resultCode === RESULT_OK) {
                //If screen lock authentication is success update text
                showLog("Auth success")
            } else {
                //If screen lock authentication is failed
                finishAffinity()
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


    fun authenticateApp() {
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

}