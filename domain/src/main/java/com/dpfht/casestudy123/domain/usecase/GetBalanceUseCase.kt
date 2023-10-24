package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.entity.db_entity.BalanceEntity

interface GetBalanceUseCase {

  suspend operator fun invoke(): Result<BalanceEntity>
}
