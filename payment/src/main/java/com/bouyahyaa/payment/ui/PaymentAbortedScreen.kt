package com.bouyahyaa.payment.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PaymentAbortedScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top Status Indicators (All Gray for Aborted)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
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
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 6.dp)
                        .background(Color.LightGray, RoundedCornerShape(3.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Operation aborted",
                fontSize = 26.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(64.dp))
            HorizontalDivider(
                color = Color.Black,
                thickness = 2.dp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(64.dp))

            // Thick Red X
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(100.dp)) {
                    // Draw first line of the X (Top-Left to Bottom-Right)
                    drawLine(
                        color = Color(0xFFD32F2F), // Dark Red
                        start = Offset(20f, 20f),
                        end = Offset(size.width - 20f, size.height - 20f),
                        strokeWidth = 24f,
                        cap = StrokeCap.Round
                    )
                    // Draw second line of the X (Top-Right to Bottom-Left)
                    drawLine(
                        color = Color(0xFFD32F2F), // Dark Red
                        start = Offset(size.width - 20f, 20f),
                        end = Offset(20f, size.height - 20f),
                        strokeWidth = 24f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}