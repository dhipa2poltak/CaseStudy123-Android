package com.dpfht.casestudy123.domain.model

data class QRCode(
  val source: String = "",
  val idTransaction: String = "",
  val merchantName: String = "",
  val nominal: Double = 0.0
)
