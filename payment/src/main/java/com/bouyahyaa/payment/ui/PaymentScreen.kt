package com.bouyahyaa.payment.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bouyahyaa.payment.models.ProxyAction
import java.util.Locale

@Composable
fun PaymentScreen(action: ProxyAction, amount: Double, onCancel: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Status Indicators (Mocked LEDs)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 6.dp)
                        .background(Color(0xFF8BC34A), RoundedCornerShape(3.dp))
                )
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 6.dp)
                        .background(Color.LightGray, RoundedCornerShape(3.dp))
                )
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 6.dp)
                        .background(Color.LightGray, RoundedCornerShape(3.dp))
                )
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 6.dp)
                        .background(Color.LightGray, RoundedCornerShape(3.dp))
                )
            }

            // Header based on Action
            val title = when (action) {
                ProxyAction.WAITING_FOR_CARD -> "PAYMENT"
                ProxyAction.PROCESSING_REFUND -> "REFUND"
                ProxyAction.VOIDING_TRANSACTION -> "VOID"
                else -> "TRANSACTION"
            }

            Text(
                text = title,
                fontSize = 22.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Amount Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Amount:",
                    fontSize = 24.sp,
                    color = Color.Black
                )
                Text(
                    text = String.format(Locale.GERMANY, "EUR %.2f", amount),
                    fontSize = 24.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Please present card",
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Mock Contactless Icon Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                MockContactlessGraphic()
            }

            Spacer(modifier = Modifier.height(48.dp))
            HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

            // Card Schemes Mock
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Mastercard", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Text("Maestro", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Text(
                    "VISA",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1F71),
                    fontSize = 20.sp
                )
                Text("V PAY", fontWeight = FontWeight.Bold, color = Color(0xFF1A1F71))
            }

            HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

            // Bottom Area with STOP Button
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                    modifier = Modifier.border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Stop",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "STOP",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MockContactlessGraphic() {
    Canvas(modifier = Modifier.size(280.dp, 160.dp)) {
        drawRoundRect(
            color = Color.Black,
            style = Stroke(width = 8f),
            cornerRadius = CornerRadius(200f, 200f)
        )
        drawArc(
            color = Color.Black,
            startAngle = -45f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = 12f, cap = StrokeCap.Round),
            topLeft = Offset(100f, 40f),
            size = Size(80f, 80f)
        )
        drawArc(
            color = Color.Black,
            startAngle = -45f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = 16f, cap = StrokeCap.Round),
            topLeft = Offset(60f, 20f),
            size = Size(120f, 120f)
        )
    }
}