package com.dpfht.casestudy123.domain.entity.asset_entity

data class TrxChartEntity(
  val type: String = "",
  val data: List<TrxEntity> = listOf()
)
