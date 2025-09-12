package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.model.AppException
import com.dpfht.casestudy123.domain.model.Result
import com.dpfht.casestudy123.domain.model.asset.TrxChart
import com.dpfht.casestudy123.domain.repository.AppRepository

class GetPortofoliosUseCaseImpl(
  private val appRepository: AppRepository
): GetPortofoliosUseCase {

  override suspend operator fun invoke(): Result<List<TrxChart>> {
    return try {
      Result.Success(appRepository.getPortofolios())
    } catch (e: AppException) {
      Result.Error(e.message)
    }
  }
}
