package com.dpfht.casestudy123.domain.model


sealed class QRISTransactionState {
  object None: QRISTransactionState()
  data class Success(val balance: Double): QRISTransactionState()
  data class NotEnoughBalance(val balance: Double): QRISTransactionState()
}
