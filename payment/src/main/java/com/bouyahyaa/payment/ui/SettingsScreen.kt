package com.bouyahyaa.payment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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