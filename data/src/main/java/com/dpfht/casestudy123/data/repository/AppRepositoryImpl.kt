package com.dpfht.casestudy123.data.repository

import com.dpfht.casestudy123.data.datasource.LocalDataSource
import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxChartEntity
import com.dpfht.casestudy123.domain.entity.db_entity.BalanceEntity
import com.dpfht.casestudy123.domain.entity.db_entity.QRISTransactionEntity
import com.dpfht.casestudy123.domain.repository.AppRepository
import io.reactivex.rxjava3.core.Observable

class AppRepositoryImpl(
  private val localDataSource: LocalDataSource
): AppRepository {

  override suspend fun getPortofolios(): List<TrxChartEntity> {
    return localDataSource.getPortofolios()
  }

  override fun getStreamIsDBInitialized(): Observable<Boolean> {
    return localDataSource.getStreamIsDBInitialized()
  }

  override suspend fun getBalance(): BalanceEntity {
    return localDataSource.getBalance()
  }

  override suspend fun postQRISTransaction(balanceEntity: BalanceEntity, qrEntity: QRCodeEntity) {
    return localDataSource.postQRISTransaction(balanceEntity, qrEntity)
  }

  override suspend fun getAllQRISTransaction(): List<QRISTransactionEntity> {
    return localDataSource.getAllQRISTransaction()
  }

  override suspend fun resetAllData() {
    return localDataSource.resetAllData()
  }
}
