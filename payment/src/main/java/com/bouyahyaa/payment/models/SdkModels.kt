package com.bouyahyaa.payment.models

data class CompatibilityReport(
    val showTipsOptions: String?,
    val pointerLocation: String?,
    val keyAttestationResult: String?,
    val adbShouldBeDisabled: String?,
    val isNfcSupportedByDevice: String?,
    val isNfcEnabled: String?,
    val isAndroidVersionSupported: String?,
    val isBatteryLevelOkay: String?,
    val isGooglePlayServicesPresent: String?
)

data class MdmConfig(
    val externalDeviceId: String,
    val token: String
)

enum class TerminalOrientation {
    PORTRAIT, LANDSCAPE
}

data class TransactionResult(
    val status: String,
    val transactionId: String? = null,
    val amount: Double? = null,
    val merchantReceiptJson: String? = null,
    val customerReceiptJson: String? = null,
    val merchantReceiptLines: List<String> = emptyList(),
    val customerReceiptLines: List<String> = emptyList()
)

data class TransactionRecord(
    val transactionId: String?,
    val amount: Double?,
    val minorUnits: Long?,
    val currencyCode: String?,
    val transactionTime: String?,
    val localTransactionTime: String?,
    val terminalId: String?,
    val transactionType: String?,
    val answerCode: String?,
    val answerText: String?,
    val cardScheme: String?,
    val maskedPan: String?,
    val cardType: String?,
    val authorizationCode: String?,
    val receiptNumber: String?,
    val traceNumber: String?,
    val refTransactionId: String?
)

data class PaginatedHistory(
    val transactions: List<TransactionRecord>,
    val pageNumber: Int,
    val numberOfPages: Int
)

data class TransactionFilter(
    val name: String,
    val value: Any? = null,
    val from: Any? = null,
    val to: Any? = null
)