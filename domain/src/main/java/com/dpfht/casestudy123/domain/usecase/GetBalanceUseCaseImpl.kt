package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.entity.db_entity.BalanceEntity
import com.dpfht.casestudy123.domain.repository.AppRepository

class GetBalanceUseCaseImpl(
  private val appRepository: AppRepository
): GetBalanceUseCase {

  override suspend operator fun invoke(): Result<BalanceEntity> {
    return try {
      Result.Success(appRepository.getBalance())
    } catch (e: Exception) {
      Result.ErrorResult(e.message ?: "")
    }
  }
}
