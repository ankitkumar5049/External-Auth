package com.example.authenticaition.util

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View

class AppLockManager(private val activity: Activity) {

    private val inactivityTimeout = 1 * 5 * 1000L // 3 minutes in milliseconds
    private val inactivityHandler = Handler(Looper.getMainLooper())
    private var inactivityRunnable: Runnable? = null
    private var locked = false

    private var authenticationUtil: AuthenticationUtil


    init {
        setupTouchListeners()
        authenticationUtil = AuthenticationUtil(activity)
    }

    private fun setupTouchListeners() {
        val rootView: View = activity.findViewById(android.R.id.content)
        rootView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                resetInactivityTimer()
            }
            false
        }
    }

    private fun resetInactivityTimer() {
        stopInactivityTimer()
        inactivityRunnable = Runnable {
            if (!activity.isFinishing && !locked) {
                locked = true
                showLockDialog()
            }
        }
        inactivityHandler.postDelayed(inactivityRunnable!!, inactivityTimeout)
    }

    private fun stopInactivityTimer() {
        inactivityRunnable?.let {
            inactivityHandler.removeCallbacks(it)
            inactivityRunnable = null
        }
    }

    private fun showLockDialog() {

//        if (activity is BaseActivity) {
//            (activity as BaseActivity).authenticateApp()
//        }
        resetInactivityTimer()

//        val dialogBuilder = AlertDialog.Builder(activity)
//        dialogBuilder.setMessage("App locked due to inactivity.")
//            .setPositiveButton("Unlock") { dialog: DialogInterface, _: Int ->
//                dialog.dismiss()
//                locked = false
//                if (activity is BaseActivity) {
//                    (activity as BaseActivity).authenticateApp()
//                }
//                resetInactivityTimer()
//            }
//            .setCancelable(false)
//        val lockDialog = dialogBuilder.create()
//        lockDialog.show()
    }

    fun onResume() {
        resetInactivityTimer()
    }

    fun onPause() {
        resetInactivityTimer()
    }


}
