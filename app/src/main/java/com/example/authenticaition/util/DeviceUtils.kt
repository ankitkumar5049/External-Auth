package com.example.authenticaition.util

import android.os.Build
import android.util.Log
import java.io.File

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
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("VirtualBox")
                    || Build.MODEL.contains("VMware")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || Build.MANUFACTURER.contains("unknown")
                    || Build.MANUFACTURER.contains("Andy")
                    || Build.MANUFACTURER.contains("Andy OS")
                    || Build.MANUFACTURER.contains("nox")
                    || Build.MANUFACTURER.contains("noxvm")
                    || Build.MANUFACTURER.contains("Xamarin")
                    || Build.MANUFACTURER.contains("VMware")
                    || Build.MANUFACTURER.contains("Bluestack")
                    || Build.HARDWARE.contains("goldfish")
                    || Build.HARDWARE.contains("ranchu")
                    || Build.HARDWARE.contains("vbox86")
                    || Build.HARDWARE.contains("vbox64")
                    || Build.HARDWARE.contains("vbox")
                    || Build.HARDWARE.contains("nox")
                    || Build.HARDWARE.contains("goldeb")
                    || Build.PRODUCT.contains("Bluestacks")
                    || Build.PRODUCT.contains("sdk_google")
                    || Build.PRODUCT.contains("google_sdk")
                    || Build.PRODUCT.contains("sdk")
                    || Build.PRODUCT.contains("sdk_x86")
                    || Build.PRODUCT.contains("vbox86p")
                    || Build.PRODUCT.contains("emulator")
                    || Build.PRODUCT.contains("simulator")
                    // for rooted device
                    || File("/system/app/Superuser.apk").exists()
                    || File("/sbin/su").exists()
                    || File("/system/bin/su").exists()
                    || File("/system/xbin/su").exists()
                    || File("/data/local/xbin/su").exists()
                    || File("/data/local/bin/su").exists()
                    || File("/system/su").exists()
                    || File("/system/bin/failsafe/su").exists()
                    || File("/data/local/su").exists()
                    || File("/su/bin").exists()
                    || File("/su/xbin").exists()
                    || File("/sbin/magisk").exists()
                    || File("/init.magisk.rc").exists()
                    || File("/system/bin/.ext/.su").exists()
                    || File("/system/xbin/mu").exists()
                    || File("/data/magisk.img").exists()
                    || File("/data/adb/magisk").exists()
                    || File("/data/adb/modules/magisk").exists()
                    || File("/dev/com.koushikdutta.superuser.daemon/").exists()
                    || File("/dev/su").exists())
        }

        fun isGenuineDevice(): Boolean {
            val model = Build.MODEL
            Log.e("ankit", "isGenuineDevice: ${model.toString()}", )
            return !isEmulator()
        }
    }
}
