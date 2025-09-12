package com.dpfht.casestudy123.data.repository

import com.dpfht.casestudy123.data.datasource.LocalDataSource
import com.dpfht.casestudy123.domain.model.QRCode
import com.dpfht.casestudy123.domain.model.asset.TrxChart
import com.dpfht.casestudy123.domain.model.db.Balance
import com.dpfht.casestudy123.domain.model.db.QRISTransaction
import com.dpfht.casestudy123.domain.repository.AppRepository
import io.reactivex.rxjava3.core.Observable

class AppRepositoryImpl(
  private val localDataSource: LocalDataSource
): AppRepository {

  override suspend fun getPortofolios(): List<TrxChart> {
    return localDataSource.getPortofolios()
  }

  override fun getStreamIsDBInitialized(): Observable<Boolean> {
    return localDataSource.getStreamIsDBInitialized()
  }

  override suspend fun getBalance(): Balance {
    return localDataSource.getBalance()
  }

  override suspend fun postQRISTransaction(balanceEntity: Balance, qrEntity: QRCode) {
    return localDataSource.postQRISTransaction(balanceEntity, qrEntity)
  }

  override suspend fun getAllQRISTransaction(): List<QRISTransaction> {
    return localDataSource.getAllQRISTransaction()
  }

  override suspend fun resetAllData() {
    return localDataSource.resetAllData()
  }
}
