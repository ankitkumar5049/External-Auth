package com.example.authenticaition.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.authenticaition.R
import com.example.authenticaition.data.retrofit.QuotesApi
import com.example.authenticaition.data.retrofit.RetrofitHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ParentActivity() {
    private var textView: TextView? = null
    private lateinit var btn: Button
    private val PERMISSION_REQUEST_READ_PHONE_STATE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        checkDeviceAuthenticity()

        textView =  findViewById(R.id.texview)
        btn =  findViewById(R.id.btn)

        btn.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }


    }



    private fun apiCall(){
        val quotesApi = RetrofitHelper.getInstance().create(QuotesApi::class.java)
        GlobalScope.launch {
            val result = quotesApi.getQuotes(1)
            if(result!=null){
                showLog(result.body().toString())
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            LOCK_REQUEST_CODE -> if (resultCode === RESULT_OK) {
//                //If screen lock authentication is success update text
//                isAppLocked = false
//                showLog("Auth success")
//            } else {
//                //If screen lock authentication is failed
////                finishAffinity()
//            }
//
//            SECURITY_SETTING_REQUEST_CODE ->
//                //When user is enabled Security settings then we don't get any kind of RESULT_OK
//                //So we need to check whether device has enabled screen lock or not
//                if (isDeviceSecure()) {
//                    //If screen lock enabled show toast and start intent to authenticate user
//                    showToast(getString(R.string.device_is_secure))
//                    authenticateApp()
//                } else {
//                    //If screen lock is not enabled just update text
//                    showToast(getString(R.string.security_device_cancelled))
//                }
//        }
//    }
//
//    override fun onBackPressed() {
//        if(!isAppLocked){ super.onBackPressed() }
//        else{finishAffinity()}
//    }


}