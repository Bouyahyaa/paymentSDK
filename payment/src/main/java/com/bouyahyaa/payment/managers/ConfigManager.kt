package com.bouyahyaa.payment.managers

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.bouyahyaa.payment.models.CompatibilityReport
import com.bouyahyaa.payment.models.MdmConfig
import com.bouyahyaa.payment.models.PaymentError
import com.bouyahyaa.payment.models.ProxyAction
import com.bouyahyaa.payment.models.TerminalOrientation
import com.bouyahyaa.payment.ui.SdkProxyActivity

class ConfigManager internal constructor() {

    // --- Core Configuration ---
    fun configureSdk(
        context: Context,
        tid: String,
        password: String,
        orientation: TerminalOrientation = TerminalOrientation.PORTRAIT,
        onSuccess: () -> Unit,
        onError: (PaymentError) -> Unit
    ) {
        if (tid.isBlank() || password.isBlank()) {
            onError(PaymentError.Unknown("INVALID_ARGS", "TID/Password required"))
            return
        }

        SdkProxyActivity.pendingCallback = { isSuccess, error ->
            if (isSuccess) onSuccess() else onError(
                error ?: PaymentError.Unknown(
                    "UNKNOWN",
                    "Failed"
                )
            )
        }
        SdkProxyActivity.launch(context, ProxyAction.CONFIGURING_TERMINAL)
    }

    fun showSettings(context: Context) {
        SdkProxyActivity.pendingCallback = { _, _ -> /* Do nothing */ }
        SdkProxyActivity.launch(context, ProxyAction.CONFIGURING_TERMINAL)
    }

    fun getUsabilityReport(callback: (CompatibilityReport) -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            callback(
                CompatibilityReport(
                    showTipsOptions = "OFF", pointerLocation = "OFF",
                    keyAttestationResult = "PASSED", adbShouldBeDisabled = "FALSE",
                    isNfcSupportedByDevice = "TRUE", isNfcEnabled = "TRUE",
                    isAndroidVersionSupported = "TRUE", isBatteryLevelOkay = "TRUE",
                    isGooglePlayServicesPresent = "TRUE"
                )
            )
        }, 500)
    }

    // --- MDM (Mobile Device Management) ---
    fun getMdmConfig(context: Context, callback: (MdmConfig?) -> Unit) {
        callback(MdmConfig(externalDeviceId = "25AWCD210834", token = "nWeePPKLbNx"))
    }

    fun personaliseMdm(
        context: Context,
        externalDeviceId: String,
        token: String,
        callback: (Boolean) -> Unit
    ) {
        SdkProxyActivity.pendingCallback = { isSuccess, _ -> callback(isSuccess) }
        SdkProxyActivity.launch(context, ProxyAction.CONFIGURING_TERMINAL)
    }

    // --- Administration & Recovery ---
    fun startRecovery(onSuccess: () -> Unit, onError: (PaymentError) -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({ onSuccess() }, 1000)
    }

    fun cancelRecovery(callback: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({ callback() }, 500)
    }

    fun checkRecoveryStatus(callback: (Boolean) -> Unit) {
        callback(false)
    }

    fun closePeriod(onSuccess: () -> Unit, onError: (PaymentError) -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({ onSuccess() }, 1500)
    }
}