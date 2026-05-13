package com.bouyahyaa.payment.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PaymentSuccessScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top Status Indicators
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

            Text(
                text = "V PAY",
                fontSize = 22.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.Black, thickness = 1.dp)
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Payment successful",
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

            // Thick Green Checkmark
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(100.dp)) {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(10f, size.height / 2f)
                        lineTo(size.width / 3f, size.height - 20f)
                        lineTo(size.width - 10f, 20f)
                    }
                    drawPath(
                        path = path,
                        color = Color(0xFF7CB342), // Light green
                        style = Stroke(
                            width = 24f,
                            cap = StrokeCap.Round,
                            join = androidx.compose.ui.graphics.StrokeJoin.Round
                        )
                    )
                }
            }
        }
    }
}