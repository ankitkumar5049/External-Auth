package com.example.authenticaition.activity

import com.example.authenticaition.R
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.Exception


class MainActivity2 : AppCompatActivity() {
    private val REQUEST_READ_PHONE_STATE = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission()
        } else {
            getSimId()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), REQUEST_READ_PHONE_STATE)
        } else {
            getSimId()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_PHONE_STATE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getSimId()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSimId() {
        try {
            val telephonyManager =
                getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val simId = telephonyManager.imei
            // Use the simId as needed
            Log.e("ankit", "getSimId: $simId", )
        }
        catch (e: Exception){
            Log.e("ankit", "getSimId: $e", )
        }
    }

}