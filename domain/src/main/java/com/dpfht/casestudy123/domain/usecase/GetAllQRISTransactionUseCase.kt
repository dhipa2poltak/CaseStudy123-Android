package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.entity.db_entity.QRISTransactionEntity

interface GetAllQRISTransactionUseCase {

  suspend operator fun invoke(): Result<List<QRISTransactionEntity>>
}
