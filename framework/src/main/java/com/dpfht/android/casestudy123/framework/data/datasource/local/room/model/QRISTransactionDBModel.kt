package com.dpfht.android.casestudy123.framework.data.datasource.local.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.converter.TypeTransmogrifier
import com.dpfht.casestudy123.domain.entity.db_entity.QRISTransactionEntity
import java.util.Date

@Entity(tableName = "tbl_qris_transaction")
@TypeConverters(TypeTransmogrifier::class)
data class QRISTransactionDBModel(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Long? = null,
  @ColumnInfo(name = "source")
  val source: String? = "",
  @ColumnInfo(name = "id_transaction")
  val idTransaction: String? = "",
  @ColumnInfo(name = "merchant_name")
  val merchantName: String? = "",
  @ColumnInfo(name = "nominal")
  val nominal: Double? = 0.0,
  @ColumnInfo(name = "transaction_date_time")
  var transactionDateTime: Date?,
)

fun QRISTransactionDBModel.toDomain(): QRISTransactionEntity {
  return QRISTransactionEntity(
    id = this.id ?: 0L,
    source = this.source ?: "",
    idTransaction = this.idTransaction ?: "",
    merchantName = this.merchantName ?: "",
    nominal = this.nominal ?: 0.0,
    transactionDateTime = this.transactionDateTime
  )
}
