package com.dpfht.android.casestudy123.framework.data.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.QRISTransactionDbModel

@Dao
interface QRISTransactionDao {

  @Query("SELECT * FROM tbl_qris_transaction ORDER BY transaction_date_time DESC")
  fun getAllQRISTransaction(): List<QRISTransactionDbModel>

  @Insert
  suspend fun insertQRISTransaction(transactionModel: QRISTransactionDbModel): Long

  @Query("DELETE FROM tbl_qris_transaction")
  suspend fun deleteAllQRISTransaction(): Int
}
