package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.entity.db_entity.QRISTransactionEntity
import com.dpfht.casestudy123.domain.repository.AppRepository

class GetAllQRISTransactionUseCaseImpl(
  private val appRepository: AppRepository
): GetAllQRISTransactionUseCase {

  override suspend operator fun invoke(): Result<List<QRISTransactionEntity>> {
    return try {
      Result.Success(appRepository.getAllQRISTransaction())
    } catch (e: Exception) {
      Result.ErrorResult(e.message ?: "")
    }
  }
}
