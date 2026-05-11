package com.bouyahyaa.payment.managers

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.bouyahyaa.payment.models.PaginatedHistory
import com.bouyahyaa.payment.models.PaymentError
import com.bouyahyaa.payment.models.ProxyAction
import com.bouyahyaa.payment.models.TransactionFilter
import com.bouyahyaa.payment.models.TransactionRecord
import com.bouyahyaa.payment.models.TransactionResult
import com.bouyahyaa.payment.ui.SdkProxyActivity
import java.util.UUID

class TransactionManager internal constructor() {

    fun processPayment(
        context: Context,
        amount: Double,
        onSuccess: (TransactionResult) -> Unit,
        onError: (PaymentError) -> Unit
    ) {
        if (amount <= 0) {
            onError(PaymentError.Unknown("INVALID_AMOUNT", "Amount must be greater than 0"))
            return
        }

        SdkProxyActivity.pendingCallback = { isSuccess, error ->
            if (isSuccess) {
                onSuccess(
                    TransactionResult(
                        status = "PAYMENT_SUCCESS",
                        transactionId = UUID.randomUUID().toString(),
                        amount = amount,
                        merchantReceiptLines = listOf(
                            "MERCHANT RECEIPT",
                            "Amount: €$amount",
                            "APPROVED"
                        ),
                        customerReceiptLines = listOf(
                            "CUSTOMER RECEIPT",
                            "Amount: €$amount",
                            "APPROVED"
                        )
                    )
                )
            } else {
                onError(error ?: PaymentError.Unknown("PAYMENT_FAILED", "Transaction failed"))
            }
        }
        SdkProxyActivity.launch(context, ProxyAction.WAITING_FOR_CARD)
    }

    fun refundPayment(
        context: Context,
        transactionId: String,
        amount: Double,
        onSuccess: (TransactionResult) -> Unit,
        onError: (PaymentError) -> Unit
    ) {
        SdkProxyActivity.pendingCallback = { isSuccess, error ->
            if (isSuccess) {
                onSuccess(
                    TransactionResult(
                        status = "REFUND_SUCCESS",
                        transactionId = transactionId,
                        amount = amount
                    )
                )
            } else {
                onError(error ?: PaymentError.Unknown("REFUND_FAILED", "Refund failed"))
            }
        }
        SdkProxyActivity.launch(context, ProxyAction.PROCESSING_REFUND)
    }

    fun voidTransaction(
        context: Context,
        transactionId: String?,
        onSuccess: (TransactionResult) -> Unit,
        onError: (PaymentError) -> Unit
    ) {
        SdkProxyActivity.pendingCallback = { isSuccess, error ->
            if (isSuccess) {
                onSuccess(
                    TransactionResult(
                        status = "VOID_SUCCESS",
                        transactionId = transactionId ?: "LAST_TRANSACTION"
                    )
                )
            } else {
                onError(error ?: PaymentError.Unknown("VOID_FAILED", "Void failed"))
            }
        }
        SdkProxyActivity.launch(context, ProxyAction.VOIDING_TRANSACTION)
    }

    fun getTransactionHistory(
        page: Int = 1,
        itemCount: Int = 20,
        filters: List<TransactionFilter>? = null,
        isMerchantScope: Boolean = true,
        onSuccess: (PaginatedHistory) -> Unit,
        onError: (PaymentError) -> Unit
    ) {
        // Mocking Rubean's TransactionHistoryApi with pagination (no UI required)
        Handler(Looper.getMainLooper()).postDelayed({
            val mockList = listOf(
                TransactionRecord(
                    transactionId = "txn_123", amount = 10.50, minorUnits = 1050,
                    currencyCode = "EUR", transactionTime = "2023-10-25T10:00:00",
                    localTransactionTime = "2023-10-25T12:00:00", terminalId = "TERM_01",
                    transactionType = "PAYMENT", answerCode = "00", answerText = "APPROVED",
                    cardScheme = "VISA", maskedPan = "**** **** **** 1234", cardType = "CREDIT",
                    authorizationCode = "AUTH1", receiptNumber = "001",
                    traceNumber = "TRC1", refTransactionId = null
                )
            )

            onSuccess(
                PaginatedHistory(
                    transactions = mockList,
                    pageNumber = page,
                    numberOfPages = 1
                )
            )
        }, 1000)
    }
}