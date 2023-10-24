package com.dpfht.casestudy123.data.repository

import com.dpfht.casestudy123.data.datasource.LocalDataSource
import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import com.dpfht.casestudy123.domain.entity.QRISTransactionState
import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.entity.VoidResult
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxChartEntity
import com.dpfht.casestudy123.domain.entity.db_entity.BalanceEntity
import com.dpfht.casestudy123.domain.entity.db_entity.QRISTransactionEntity
import com.dpfht.casestudy123.domain.repository.AppRepository
import io.reactivex.rxjava3.core.Observable

class AppRepositoryImpl(
  private val localDataSource: LocalDataSource
): AppRepository {

  override suspend fun getPortofolios(): Result<List<TrxChartEntity>> {
    return localDataSource.getPortofolios()
  }

  override fun getStreamIsDBInitialized(): Observable<Boolean> {
    return localDataSource.getStreamIsDBInitialized()
  }

  override suspend fun getBalance(): Result<BalanceEntity> {
    return localDataSource.getBalance()
  }

  override suspend fun postQRISTransaction(entity: QRCodeEntity): Result<QRISTransactionState> {
    return localDataSource.postQRISTransaction(entity)
  }

  override suspend fun getAllQRISTransaction(): Result<List<QRISTransactionEntity>> {
    return localDataSource.getAllQRISTransaction()
  }

  override suspend fun resetAllData(): VoidResult {
    return localDataSource.resetAllData()
  }
}
