package com.bouyahyaa.payment.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bouyahyaa.payment.models.PaymentError
import com.bouyahyaa.payment.models.ProxyAction

internal class SdkProxyActivity : ComponentActivity() {

    companion object {
        private const val EXTRA_ACTION = "EXTRA_ACTION"
        var pendingCallback: ((Boolean, PaymentError?) -> Unit)? = null

        /**
         * Updated launch method that now takes a ProxyAction
         */
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

        // Read the action to determine what text to show
        val actionString = intent.getStringExtra(EXTRA_ACTION)
        val action = runCatching { ProxyAction.valueOf(actionString ?: "") }
            .getOrDefault(ProxyAction.WAITING_FOR_CARD)

        // Draw the Mock UI
        setContent {
            // A semi-transparent dark background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                // The floating dialog card
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
                    }
                }
            }
        }

        // Simulate the delay of the hardware interaction
        val delayMs = if (action == ProxyAction.WAITING_FOR_CARD) 3000L else 1500L

        Handler(Looper.getMainLooper()).postDelayed({
            pendingCallback?.invoke(true, null)
            pendingCallback = null
            finish()
        }, delayMs)
    }

    private fun getDisplayTextForAction(action: ProxyAction): String =
        when (action) {
            ProxyAction.CONFIGURING_TERMINAL -> "Configuring Terminal..."
            ProxyAction.WAITING_FOR_CARD -> "Please Tap Card on Back of Phone"
            ProxyAction.PROCESSING_REFUND -> "Processing Refund..."
            ProxyAction.VOIDING_TRANSACTION -> "Voiding Transaction..."
        }
}