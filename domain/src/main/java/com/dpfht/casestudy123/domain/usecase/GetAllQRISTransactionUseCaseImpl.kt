package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.model.AppException
import com.dpfht.casestudy123.domain.model.Result
import com.dpfht.casestudy123.domain.model.db.QRISTransaction
import com.dpfht.casestudy123.domain.repository.AppRepository

class GetAllQRISTransactionUseCaseImpl(
  private val appRepository: AppRepository
): GetAllQRISTransactionUseCase {

  override suspend operator fun invoke(): Result<List<QRISTransaction>> {
    return try {
      Result.Success(appRepository.getAllQRISTransaction())
    } catch (e: AppException) {
      Result.Error(e.message)
    }
  }
}
