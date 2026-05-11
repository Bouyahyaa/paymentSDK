package com.bouyahyaa.payment.models

sealed class PaymentError(val rawCode: String, val message: String) {
    class BatteryLow(message: String = "Battery too low to process payments.") :
        PaymentError("BATTERY_LOW", message)

    class Network(rawCode: String, message: String) : PaymentError(rawCode, message)
    class UserCancelled : PaymentError("CANCELLED", "The user cancelled the process.")
    class AppOutdated : PaymentError("OUTDATED", "The payment core application requires an update.")
    class Unknown(rawCode: String, message: String) : PaymentError(rawCode, message)
}