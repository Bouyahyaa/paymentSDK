package com.bouyahyaa.payment.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PersonalisingScreen() {
    // Surface fills the entire screen with a clean white background
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // A secure lock icon to represent authentication/cryptography
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Secure Authentication",
                tint = Color(0xFF0055FF), // SDK Brand Blue
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // A larger, thicker loading spinner
            CircularProgressIndicator(
                color = Color(0xFF0055FF),
                strokeWidth = 6.dp,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Personalising...",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Supportive subtitle text
            Text(
                text = "Securely configuring your terminal.\nPlease do not close the app.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}