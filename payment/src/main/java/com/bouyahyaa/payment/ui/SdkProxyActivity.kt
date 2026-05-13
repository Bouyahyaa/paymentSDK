package com.bouyahyaa.payment.ui

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.bouyahyaa.payment.models.PaymentError
import com.bouyahyaa.payment.models.ProxyAction

internal class SdkProxyActivity : ComponentActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var currentAction: ProxyAction
    private var transactionAmount: Double = 0.0
    private var isTaskCompleted = false

    private val cardTapActions = listOf(
        ProxyAction.WAITING_FOR_CARD,
        ProxyAction.PROCESSING_REFUND,
        ProxyAction.VOIDING_TRANSACTION
    )

    companion object {
        private const val EXTRA_ACTION = "EXTRA_ACTION"
        private const val EXTRA_AMOUNT = "EXTRA_AMOUNT"
        var pendingCallback: ((Boolean, PaymentError?) -> Unit)? = null

        fun launch(context: Context, action: ProxyAction, amount: Double = 0.0) {
            val intent = Intent(context, SdkProxyActivity::class.java).apply {
                putExtra(EXTRA_ACTION, action.name)
                putExtra(EXTRA_AMOUNT, amount)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionString = intent.getStringExtra(EXTRA_ACTION)
        transactionAmount = intent.getDoubleExtra(EXTRA_AMOUNT, 0.0)

        val parsedAction = runCatching { ProxyAction.valueOf(actionString ?: "") }.getOrNull()
        if (parsedAction == null) {
            finishWithResult(
                false,
                PaymentError.Unknown("INVALID_ACTION", "Proxy launched without a valid action.")
            )
            return
        }

        currentAction = parsedAction
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        setContent {
            when (currentAction) {
                ProxyAction.SHOWING_SETTINGS -> {
                    SettingsScreen(onClose = { finishWithResult(true, null) })
                }

                in cardTapActions -> {
                    PaymentScreen(
                        action = currentAction,
                        amount = transactionAmount,
                        onCancel = { finishWithResult(false, PaymentError.UserCancelled()) }
                    )
                }

                else -> {
                    LoadingDialogScreen(
                        action = currentAction,
                        onCancel = { finishWithResult(false, PaymentError.UserCancelled()) }
                    )
                }
            }
        }

        if (currentAction != ProxyAction.SHOWING_SETTINGS && currentAction !in cardTapActions) {
            Handler(Looper.getMainLooper()).postDelayed({
                finishWithResult(true, null)
            }, 1500L)
        }
    }

    override fun onResume() {
        super.onResume()
        if (currentAction in cardTapActions && nfcAdapter != null) {
            val flags = NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or
                    NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS

            nfcAdapter?.enableReaderMode(this, this, flags, null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTagDiscovered(tag: Tag?) {
        if (isTaskCompleted) return

        try {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
            vibrator.vibrate(
                android.os.VibrationEffect.createOneShot(
                    100,
                    android.os.VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } catch (e: Exception) {
            android.util.Log.w(
                "SdkProxyActivity",
                "Missing vibrate permission, skipping vibration."
            )
        }

        runOnUiThread {
            finishWithResult(true, null)
        }
    }

    override fun onPause() {
        super.onPause()
        if (currentAction in cardTapActions && nfcAdapter != null) {
            nfcAdapter?.disableReaderMode(this)
        }
    }

    private fun finishWithResult(isSuccess: Boolean, error: PaymentError?) {
        if (isTaskCompleted) return
        isTaskCompleted = true

        pendingCallback?.invoke(isSuccess, error)
        pendingCallback = null
        finish()
    }
}