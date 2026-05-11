package com.bouyahyaa.payment.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bouyahyaa.payment.models.PaymentError

/**
 * A hidden activity that handles `startActivityForResult` requirements
 * without forcing the host app to manage activity lifecycles.
 */
internal class SdkProxyActivity : Activity() {

    companion object {
        var pendingCallback: ((Boolean, PaymentError?) -> Unit)? = null

        /**
         * Helper method to easily launch this proxy activity from anywhere in the SDK.
         */
        fun launch(context: Context) {
            val intent = Intent(context, SdkProxyActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mocking a successful UI interaction delay
        Handler(Looper.getMainLooper()).postDelayed({
            pendingCallback?.invoke(true, null)

            // Clean up and close the transparent activity
            pendingCallback = null
            finish()
        }, 1500)
    }
}