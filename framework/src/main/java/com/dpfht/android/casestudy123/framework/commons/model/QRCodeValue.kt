package com.dpfht.android.casestudy123.framework.commons.model

import androidx.annotation.Keep
import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class QRCodeValue(
  @SerializedName("source")
  @Expose
  val source: String? = "",
  @SerializedName("id_transaction")
  @Expose
  val idTransaction: String? = "",
  @SerializedName("merchant_name")
  @Expose
  val merchantName: String? = "",
  @SerializedName("nominal")
  @Expose
  val nominal: Double? = 0.0
)

fun QRCodeValue.toDomain(): QRCodeEntity {
  return QRCodeEntity(
    source = this.source ?: "",
    idTransaction = this.idTransaction ?: "",
    merchantName = this.merchantName ?: "",
    nominal = this.nominal ?: 0.0
  )
}
