package com.bouyahyaa.payment

import com.bouyahyaa.payment.models.ProxyAction

fun getDisplayTextForAction(action: ProxyAction): String =
    when (action) {
        ProxyAction.CONFIGURING_TERMINAL -> "Configuring Terminal..."
        ProxyAction.WAITING_FOR_CARD -> "Please Tap Card on Back of Phone"
        ProxyAction.PROCESSING_REFUND -> "Processing Refund..."
        ProxyAction.VOIDING_TRANSACTION -> "Voiding Transaction..."
        ProxyAction.SHOWING_SETTINGS -> ""
    }