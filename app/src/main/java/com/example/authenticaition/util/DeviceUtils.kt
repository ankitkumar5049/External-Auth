package com.example.authenticaition.util

import android.os.Build
import android.util.Log

class DeviceUtils {
    companion object {
//        private val knownDeviceModels = arrayOf(
//            // List of known genuine device models
//            "Pixel 2",
//            "Pixel 3",
//            // Add more genuine device models here
//        )

        fun isEmulator(): Boolean {
            return (Build.FINGERPRINT.contains("generic")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || Build.HARDWARE.contains("goldfish")
                    || Build.HARDWARE.contains("ranchu")
                    || Build.HARDWARE.contains("vbox86")
                    || Build.HARDWARE.contains("vbox64")
                    || Build.PRODUCT.contains("sdk_google")
                    || Build.PRODUCT.contains("google_sdk")
                    || Build.PRODUCT.contains("sdk")
                    || Build.PRODUCT.contains("sdk_x86")
                    || Build.PRODUCT.contains("vbox86p")
                    || Build.PRODUCT.contains("emulator")
                    || Build.PRODUCT.contains("simulator"))
        }

        fun isGenuineDevice(): Boolean {
            val model = Build.MODEL
            Log.e("ankit", "isGenuineDevice: ${model.toString()}", )
//            return !isEmulator() && knownDeviceModels.contains(model)
            return !isEmulator()
        }
    }
}
