package com.dpfht.casestudy123.domain.repository

import com.dpfht.casestudy123.domain.model.QRCode
import com.dpfht.casestudy123.domain.model.asset.TrxChart
import com.dpfht.casestudy123.domain.model.db.Balance
import com.dpfht.casestudy123.domain.model.db.QRISTransaction
import io.reactivex.rxjava3.core.Observable

interface AppRepository {

  suspend fun getPortofolios(): List<TrxChart>
  fun getStreamIsDBInitialized(): Observable<Boolean>
  suspend fun getBalance(): Balance
  suspend fun postQRISTransaction(balanceEntity: Balance, qrEntity: QRCode)
  suspend fun getAllQRISTransaction(): List<QRISTransaction>
  suspend fun resetAllData()
}
