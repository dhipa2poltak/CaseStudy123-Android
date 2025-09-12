package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.model.Result
import com.dpfht.casestudy123.domain.model.db.QRISTransaction

interface GetAllQRISTransactionUseCase {

  suspend operator fun invoke(): Result<List<QRISTransaction>>
}
