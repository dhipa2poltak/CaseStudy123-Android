package com.dpfht.casestudy123.domain.model.db

import java.util.Date

data class QRISTransaction(
  val id: Long = 0L,
  val source: String = "",
  val idTransaction: String = "",
  val merchantName: String = "",
  val nominal: Double = 0.0,
  var transactionDateTime: Date? = null,
)
