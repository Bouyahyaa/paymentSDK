package com.bouyahyaa.payment.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CardSchemeAnimationScreen() {
    var visible by remember { mutableStateOf(false) }

    // Trigger the animation immediately when the screen enters the composition
    LaunchedEffect(Unit) {
        visible = true
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Box(contentAlignment = Alignment.Center) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(500)) + scaleIn(
                    initialScale = 0.3f,
                    animationSpec = tween(500)
                )
            ) {
                Text(
                    text = "VISA",
                    color = Color(0xFF1A1F71), // Official Visa Blue
                    fontSize = 80.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}