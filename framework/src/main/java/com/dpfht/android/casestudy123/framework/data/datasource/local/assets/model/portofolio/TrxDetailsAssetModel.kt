package com.dpfht.android.casestudy123.framework.data.datasource.local.assets.model.portofolio

import androidx.annotation.Keep
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxDetailsEntity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class TrxDetailsAssetModel(
  @SerializedName("trx_date")
  @Expose
  val trxDate: String? = "",
  @SerializedName("nominal")
  @Expose
  val nominal: Double? = 0.0
)

fun TrxDetailsAssetModel.toDomain(): TrxDetailsEntity {
  return TrxDetailsEntity(
    trxDate = this.trxDate ?: "",
    nominal = this.nominal ?: 0.0
  )
}
