package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.model.AppException
import com.dpfht.casestudy123.domain.model.Result
import com.dpfht.casestudy123.domain.model.db.Balance
import com.dpfht.casestudy123.domain.repository.AppRepository

class GetBalanceUseCaseImpl(
  private val appRepository: AppRepository
): GetBalanceUseCase {

  override suspend operator fun invoke(): Result<Balance> {
    return try {
      Result.Success(appRepository.getBalance())
    } catch (e: AppException) {
      Result.Error(e.message)
    }
  }
}
