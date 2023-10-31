package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.VoidResult
import com.dpfht.casestudy123.domain.repository.AppRepository

class ResetAllDataUseCaseImpl(
  private val appRepository: AppRepository
): ResetAllDataUseCase {

  override suspend operator fun invoke(): VoidResult {
    return try {
      appRepository.resetAllData()

      VoidResult.Success
    } catch (e: Exception) {
      VoidResult.Error(e.message ?: "")
    }
  }
}
