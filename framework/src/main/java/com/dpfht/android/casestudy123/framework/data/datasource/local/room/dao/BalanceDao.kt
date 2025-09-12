package com.dpfht.android.casestudy123.framework.data.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.BalanceDbModel

@Dao
interface BalanceDao {

  @Query("SELECT * FROM tbl_balance WHERE type = :type")
  fun getBalance(type: String): List<BalanceDbModel>

  @Insert
  suspend fun insertBalance(balanceModel: BalanceDbModel): Long

  @Update
  suspend fun updateBalance(balanceModel: BalanceDbModel): Int
}
