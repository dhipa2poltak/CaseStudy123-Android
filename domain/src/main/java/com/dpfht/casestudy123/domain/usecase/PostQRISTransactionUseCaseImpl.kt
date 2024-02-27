package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.AppException
import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import com.dpfht.casestudy123.domain.entity.QRISTransactionState
import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.repository.AppRepository

class PostQRISTransactionUseCaseImpl(
  private val appRepository: AppRepository
): PostQRISTransactionUseCase {

  override suspend operator fun invoke(qrEntity: QRCodeEntity): Result<QRISTransactionState> {
    try {
      val balanceEntity = appRepository.getBalance()
      val theBalance = balanceEntity.balance
      if (theBalance == 0.0 && qrEntity.nominal > 0.0) {
        return Result.Success(QRISTransactionState.NotEnoughBalance(theBalance))
      }

      val newBalance = theBalance - qrEntity.nominal
      if (newBalance < 0) {
        return Result.Success(QRISTransactionState.NotEnoughBalance(theBalance))
      }

      appRepository.postQRISTransaction(balanceEntity.copy(balance = newBalance), qrEntity)

      return Result.Success(QRISTransactionState.Success(newBalance))
    } catch (e: AppException) {
      return Result.Error(e.message)
    }
  }
}
