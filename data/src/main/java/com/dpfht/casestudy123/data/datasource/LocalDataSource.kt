package com.dpfht.casestudy123.data.datasource

import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import com.dpfht.casestudy123.domain.entity.QRISTransactionState
import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.entity.VoidResult
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxChartEntity
import com.dpfht.casestudy123.domain.entity.db_entity.BalanceEntity
import com.dpfht.casestudy123.domain.entity.db_entity.QRISTransactionEntity
import io.reactivex.rxjava3.core.Observable

interface LocalDataSource {

  suspend fun getPortofolios(): Result<List<TrxChartEntity>>
  fun getStreamIsDBInitialized(): Observable<Boolean>
  suspend fun getBalance(): Result<BalanceEntity>
  suspend fun postQRISTransaction(entity: QRCodeEntity): Result<QRISTransactionState>
  suspend fun getAllQRISTransaction(): Result<List<QRISTransactionEntity>>
  suspend fun resetAllData(): VoidResult
}
