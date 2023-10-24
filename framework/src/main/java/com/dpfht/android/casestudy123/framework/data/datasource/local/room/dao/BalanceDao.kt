package com.dpfht.android.casestudy123.framework.data.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.BalanceDBModel

@Dao
interface BalanceDao {

  @Query("SELECT * FROM tbl_balance WHERE type = :type")
  fun getBalance(type: String): List<BalanceDBModel>

  @Insert
  suspend fun insertBalance(balanceModel: BalanceDBModel): Long

  @Update
  suspend fun updateBalance(balanceModel: BalanceDBModel): Int
}
