package com.dpfht.casestudy123.domain.repository

import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import com.dpfht.casestudy123.domain.entity.QRISTransactionState
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxChartEntity
import com.dpfht.casestudy123.domain.entity.db_entity.BalanceEntity
import com.dpfht.casestudy123.domain.entity.db_entity.QRISTransactionEntity
import io.reactivex.rxjava3.core.Observable

interface AppRepository {

  suspend fun getPortofolios(): List<TrxChartEntity>
  fun getStreamIsDBInitialized(): Observable<Boolean>
  suspend fun getBalance(): BalanceEntity
  suspend fun postQRISTransaction(entity: QRCodeEntity): QRISTransactionState
  suspend fun getAllQRISTransaction(): List<QRISTransactionEntity>
  suspend fun resetAllData()
}
