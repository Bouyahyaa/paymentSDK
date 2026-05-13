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
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    private var isTaskCompleted = false

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

        val actionString = intent.getStringExtra(EXTRA_ACTION)
        currentAction = runCatching { ProxyAction.valueOf(actionString ?: "") }
            .getOrDefault(ProxyAction.WAITING_FOR_CARD)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        setContent {
            // Decide which UI to show based on the action
            if (currentAction == ProxyAction.SHOWING_SETTINGS) {
                SettingsScreen(onClose = { finishWithResult(true, null) })
            } else {
                LoadingDialogScreen(currentAction, onCancel = {
                    finishWithResult(false, PaymentError.UserCancelled())
                })
            }
        }

        // Only auto-finish if it's a generic loading action.
        // We DO NOT auto-finish for settings (user must click close) or waiting for card (physical tap).
        if (currentAction != ProxyAction.WAITING_FOR_CARD && currentAction != ProxyAction.SHOWING_SETTINGS) {
            Handler(Looper.getMainLooper()).postDelayed({
                finishWithResult(true, null)
            }, 1500L)
        }
    }

    override fun onResume() {
        super.onResume()
        if (currentAction == ProxyAction.WAITING_FOR_CARD && nfcAdapter != null) {
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
}

// ============================================================================
// COMPOSABLE UI SCREENS
// ============================================================================

@Composable
fun LoadingDialogScreen(action: ProxyAction, onCancel: () -> Unit) {
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
                    text = getDisplayTextForAction(action),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )

                if (action == ProxyAction.WAITING_FOR_CARD) {
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(onClick = onCancel) {
                        Text("Cancel", color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(onClose: () -> Unit) {
    // State variables for our fake settings
    var soundsEnabled by remember { mutableStateOf(true) }
    var autoPrint by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5) // Light gray background
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0055FF)) // SDK Brand Color
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Terminal Settings",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onClose) {
                    Text("Done", color = Color.White, fontSize = 16.sp)
                }
            }

            // Body
            Column(modifier = Modifier.padding(16.dp)) {

                // Section 1: Info
                Text(
                    text = "DEVICE INFO",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Terminal ID: 12345678", color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("SDK Version: 1.0.0-mock", color = Color.DarkGray)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section 2: Preferences
                Text(
                    text = "PREFERENCES",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "NFC Interaction Sounds",
                                modifier = Modifier.weight(1f),
                                color = Color.DarkGray
                            )
                            Switch(
                                checked = soundsEnabled,
                                onCheckedChange = { soundsEnabled = it }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Auto-Print Receipts",
                                modifier = Modifier.weight(1f),
                                color = Color.DarkGray
                            )
                            Switch(
                                checked = autoPrint,
                                onCheckedChange = { autoPrint = it }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getDisplayTextForAction(action: ProxyAction): String {
    return when (action) {
        ProxyAction.CONFIGURING_TERMINAL -> "Configuring Terminal..."
        ProxyAction.WAITING_FOR_CARD -> "Please Tap Card on Back of Phone"
        ProxyAction.PROCESSING_REFUND -> "Processing Refund..."
        ProxyAction.VOIDING_TRANSACTION -> "Voiding Transaction..."
        ProxyAction.SHOWING_SETTINGS -> ""
    }
}