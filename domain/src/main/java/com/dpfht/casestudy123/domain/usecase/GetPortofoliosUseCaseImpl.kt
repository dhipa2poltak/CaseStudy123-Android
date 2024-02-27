package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.AppException
import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxChartEntity
import com.dpfht.casestudy123.domain.repository.AppRepository

class GetPortofoliosUseCaseImpl(
  private val appRepository: AppRepository
): GetPortofoliosUseCase {

  override suspend operator fun invoke(): Result<List<TrxChartEntity>> {
    return try {
      Result.Success(appRepository.getPortofolios())
    } catch (e: AppException) {
      Result.Error(e.message)
    }
  }
}
