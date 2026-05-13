package com.bouyahyaa.payment.models

enum class ProxyAction {
    CONFIGURING_TERMINAL,
    WAITING_FOR_CARD,
    PROCESSING_REFUND,
    VOIDING_TRANSACTION,

    SHOWING_SETTINGS
}