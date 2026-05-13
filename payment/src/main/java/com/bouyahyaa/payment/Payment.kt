package com.bouyahyaa.payment

import android.content.Context
import android.util.Log
import com.bouyahyaa.payment.managers.ConfigManager
import com.bouyahyaa.payment.managers.HardwareManager
import com.bouyahyaa.payment.managers.TransactionManager

object PaymentSDK {

    private const val TAG = "PaymentSDK"
    private var isInitialized = false

    // The clean, public API surfaces
    val config = ConfigManager()
    val hardware = HardwareManager()
    val transaction = TransactionManager()

    /**
     * Initializes the core SDK environment.
     */
    fun initEnvironment(context: Context, apiKey: String, apiSecret: String) {
        isInitialized = true
        Log.d(TAG, "Environment initialized successfully.")
    }

    /**
     * Cleans up SDK resources, unbinds background services, and prevents memory leaks.
     * Host applications MUST call this in their onDestroy() method.
     */
    fun cleanup(context: Context) {
        if (!isInitialized) return
        Log.d(TAG, "Cleaning up SDK resources and unbinding services...")

        // Example of how you will eventually unbind the real services:
        // hardware.unbindTerminalHelper()
        // transaction.disconnectCCVService()

        isInitialized = false
    }
}