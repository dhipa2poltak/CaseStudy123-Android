package com.dpfht.android.casestudy123.framework.data.datasource.local.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dpfht.casestudy123.domain.entity.db_entity.BalanceEntity

@Entity(tableName = "tbl_balance")
data class BalanceDBModel(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Long? = null,
  @ColumnInfo(name = "type")
  val type: String? = "",
  @ColumnInfo(name = "balance")
  val balance: Double? = 0.0
)


fun BalanceDBModel.toDomain(): BalanceEntity {
  return BalanceEntity(
    type = this.type ?: "",
    balance = this.balance ?: 0.0
  )
}
