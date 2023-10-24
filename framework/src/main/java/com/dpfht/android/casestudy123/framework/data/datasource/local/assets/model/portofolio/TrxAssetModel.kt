package com.dpfht.android.casestudy123.framework.data.datasource.local.assets.model.portofolio

import androidx.annotation.Keep
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxEntity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class TrxAssetModel(
  @SerializedName("label")
  @Expose
  val label: String? = "",
  @SerializedName("percentage")
  @Expose
  val percentage: String? = "",
  @SerializedName("data")
  @Expose
  val data: List<TrxDetailsAssetModel>? = listOf()
)

fun TrxAssetModel.toDomain(): TrxEntity {
  return TrxEntity(
    label = this.label ?: "",
    percentage = this.percentage ?: "0",
    data = this.data?.map { it.toDomain() } ?: listOf()
  )
}
