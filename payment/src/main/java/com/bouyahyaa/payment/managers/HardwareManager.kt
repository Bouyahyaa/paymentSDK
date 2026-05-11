package com.bouyahyaa.payment.managers

import android.content.Context
import android.media.AudioManager
import android.util.Log
import com.bouyahyaa.payment.models.TerminalOrientation
import com.bouyahyaa.payment.models.PaymentError

class HardwareManager internal constructor() {

    private val TAG = "HardwareManager"

    fun isTerminalOperational(callback: (Boolean) -> Unit) {
        callback(true)
    }

    fun setTerminalOrientation(
        orientation: TerminalOrientation,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d(TAG, "Mocking IPC bind to update orientation to $orientation")
        onSuccess()
    }

    private val nfcRelatedStreams = listOf(
        AudioManager.STREAM_SYSTEM, AudioManager.STREAM_NOTIFICATION,
        AudioManager.STREAM_RING, AudioManager.STREAM_ALARM, AudioManager.STREAM_MUSIC
    )

    fun getNfcMaxVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val positiveMaxes =
            nfcRelatedStreams.map { audioManager.getStreamMaxVolume(it) }.filter { it > 0 }
        return (positiveMaxes.minOrNull() ?: 5).coerceAtLeast(1)
    }

    fun setNfcChirpVolume(context: Context, level: Int): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxLevel = getNfcMaxVolume(context)
        val targetVolume = (level - 1).coerceIn(0, maxLevel)

        nfcRelatedStreams.forEach { stream ->
            val maxVolume = audioManager.getStreamMaxVolume(stream)
            val safeVolume = targetVolume.coerceAtMost(maxVolume)
            audioManager.setStreamVolume(stream, safeVolume, 0)
        }
        return targetVolume + 1
    }

    fun printCustomerReceipt(
        context: Context,
        lines: List<String>,
        qrCodeBytes: ByteArray?,
        onSuccess: () -> Unit,
        onError: (PaymentError) -> Unit
    ) {
        if (lines.isEmpty()) {
            onError(PaymentError.Unknown("NO_LINES", "No receipt lines provided"))
            return
        }

        Log.d(TAG, "Preparing PDF Canvas for ${lines.size} lines...")
        Log.d(TAG, "Sending job to PrintManager: Receipt_${System.currentTimeMillis()}")
        onSuccess()
    }
}