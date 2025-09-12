package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.model.Result
import com.dpfht.casestudy123.domain.model.db.Balance

interface GetBalanceUseCase {

  suspend operator fun invoke(): Result<Balance>
}
