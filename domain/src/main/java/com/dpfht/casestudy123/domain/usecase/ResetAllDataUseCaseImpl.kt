package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.model.AppException
import com.dpfht.casestudy123.domain.model.VoidResult
import com.dpfht.casestudy123.domain.repository.AppRepository

class ResetAllDataUseCaseImpl(
  private val appRepository: AppRepository
): ResetAllDataUseCase {

  override suspend operator fun invoke(): VoidResult {
    return try {
      appRepository.resetAllData()

      VoidResult.Success
    } catch (e: AppException) {
      VoidResult.Error(e.message)
    }
  }
}
