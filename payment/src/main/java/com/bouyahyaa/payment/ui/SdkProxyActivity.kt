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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bouyahyaa.payment.models.PaymentError
import com.bouyahyaa.payment.models.ProxyAction

internal class SdkProxyActivity : ComponentActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null
    private var currentAction: ProxyAction = ProxyAction.WAITING_FOR_CARD
    private var isTaskCompleted = false // Prevents double-firing

    companion object {
        private const val EXTRA_ACTION = "EXTRA_ACTION"
        var pendingCallback: ((Boolean, PaymentError?) -> Unit)? = null

        fun launch(context: Context, action: ProxyAction) {
            val intent = Intent(context, SdkProxyActivity::class.java).apply {
                putExtra(EXTRA_ACTION, action.name)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Read the action
        val actionString = intent.getStringExtra(EXTRA_ACTION)
        currentAction = runCatching { ProxyAction.valueOf(actionString ?: "") }
            .getOrDefault(ProxyAction.WAITING_FOR_CARD)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // Draw the UI
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.padding(32.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        CircularProgressIndicator(color = Color(0xFF0055FF))
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = getDisplayTextForAction(currentAction),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Give them a cancel button just in case they don't have a card nearby!
                        TextButton(onClick = {
                            finishWithResult(
                                false,
                                PaymentError.UserCancelled()
                            )
                        }) {
                            Text("Cancel", color = Color.Red)
                        }
                    }
                }
            }
        }

        // If it's NOT a payment tap, we just simulate a delay like before
        if (currentAction != ProxyAction.WAITING_FOR_CARD) {
            Handler(Looper.getMainLooper()).postDelayed({
                finishWithResult(true, null)
            }, 1500L)
        }
        // If it IS WAITING_FOR_CARD, we do nothing here and wait for the physical NFC tap!
    }

    override fun onResume() {
        super.onResume()
        // Start listening for physical NFC cards when the activity is visible
        if (currentAction == ProxyAction.WAITING_FOR_CARD && nfcAdapter != null) {

            // CRITICAL FIX: You MUST include FLAG_READER_NO_PLATFORM_SOUNDS.
            // This prevents the Android OS from stealing the card tap and freezing your dialog!
            val flags = NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or
                    NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS

            nfcAdapter?.enableReaderMode(this, this, flags, null)
        }
    }

    /**
     * Triggered by Android hardware when a REAL card or phone is tapped!
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTagDiscovered(tag: Tag?) {
        if (isTaskCompleted) return

        // 1. Play our own vibration so you know the hardware read the card successfully
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

        // 2. We MUST run the finish logic on the Main UI Thread
        runOnUiThread {
            // Close the dialog and trigger the success callback in the host app!
            finishWithResult(true, null)
        }
    }

    override fun onPause() {
        super.onPause()
        // Stop listening for NFC when the activity goes into the background
        if (currentAction == ProxyAction.WAITING_FOR_CARD && nfcAdapter != null) {
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

    private fun getDisplayTextForAction(action: ProxyAction): String {
        return when (action) {
            ProxyAction.CONFIGURING_TERMINAL -> "Configuring Terminal..."
            ProxyAction.WAITING_FOR_CARD -> "Please Tap Card on Back of Phone"
            ProxyAction.PROCESSING_REFUND -> "Processing Refund..."
            ProxyAction.VOIDING_TRANSACTION -> "Voiding Transaction..."
        }
    }
}