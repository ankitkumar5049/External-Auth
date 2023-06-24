package com.example.authenticaition

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.biometric.BiometricPrompt
import com.example.authenticaition.util.AuthenticationUtil

class MainActivity : BaseActivity() {
    private val LOCK_REQUEST_CODE = 221
    private val SECURITY_SETTING_REQUEST_CODE = 233
    private var textView: TextView? = null

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private lateinit var inactivityLock: AppLockManager
    private lateinit var authenticationUtil: AuthenticationUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView =  findViewById(R.id.texview);
        inactivityLock = AppLockManager(this)

        authenticateApp()

    }

    fun authenticateAgain(view: View) {
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
    }


}