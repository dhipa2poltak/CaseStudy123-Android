package com.dpfht.casestudy123.domain.entity

data class QRCodeEntity(
  val source: String = "",
  val idTransaction: String = "",
  val merchantName: String = "",
  val nominal: Double = 0.0
)
