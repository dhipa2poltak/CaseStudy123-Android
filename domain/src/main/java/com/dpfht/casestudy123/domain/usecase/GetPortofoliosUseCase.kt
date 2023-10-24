package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxChartEntity

interface GetPortofoliosUseCase {

  suspend operator fun invoke(): Result<List<TrxChartEntity>>
}

