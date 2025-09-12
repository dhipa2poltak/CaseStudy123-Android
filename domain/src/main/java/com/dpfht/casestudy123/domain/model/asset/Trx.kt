package com.dpfht.casestudy123.domain.model.asset

data class Trx(
  val label: String = "",
  val percentage: String = "",
  val data: List<TrxDetails> = listOf()
)
