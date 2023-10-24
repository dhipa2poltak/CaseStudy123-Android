package com.dpfht.casestudy123.domain.entity.asset_entity

data class TrxEntity(
  val label: String = "",
  val percentage: String = "",
  val data: List<TrxDetailsEntity> = listOf()
)
