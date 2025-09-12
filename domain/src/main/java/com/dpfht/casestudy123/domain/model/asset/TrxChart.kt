package com.dpfht.casestudy123.domain.model.asset

data class TrxChart(
  val type: String = "",
  val data: List<Trx> = listOf()
)
