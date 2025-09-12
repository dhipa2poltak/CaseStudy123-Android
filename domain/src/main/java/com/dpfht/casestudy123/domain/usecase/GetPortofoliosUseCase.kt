package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.model.Result
import com.dpfht.casestudy123.domain.model.asset.TrxChart

interface GetPortofoliosUseCase {

  suspend operator fun invoke(): Result<List<TrxChart>>
}

