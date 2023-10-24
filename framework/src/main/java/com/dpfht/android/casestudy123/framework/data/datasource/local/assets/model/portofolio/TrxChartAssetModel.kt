package com.dpfht.android.casestudy123.framework.data.datasource.local.assets.model.portofolio

import androidx.annotation.Keep
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxChartEntity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class TrxChartAssetModel(
  @SerializedName("type")
  @Expose
  val type: String? = "",
  @SerializedName("data")
  @Expose
  val data: List<TrxAssetModel>? = listOf()
)

fun TrxChartAssetModel.toDomain(): TrxChartEntity {
  return TrxChartEntity(
    type = this.type ?: "",
    data = this.data?.map { it.toDomain() } ?: listOf()
  )
}
