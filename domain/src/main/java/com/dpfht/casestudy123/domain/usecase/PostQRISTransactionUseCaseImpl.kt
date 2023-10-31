package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import com.dpfht.casestudy123.domain.entity.QRISTransactionState
import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.repository.AppRepository

class PostQRISTransactionUseCaseImpl(
  private val appRepository: AppRepository
): PostQRISTransactionUseCase {

  override suspend operator fun invoke(entity: QRCodeEntity): Result<QRISTransactionState> {
    return try {
      Result.Success(appRepository.postQRISTransaction(entity))
    } catch (e: Exception) {
      Result.ErrorResult(e.message ?: "")
    }
  }
}
