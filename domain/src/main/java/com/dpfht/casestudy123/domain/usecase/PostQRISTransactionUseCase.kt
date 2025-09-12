package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.model.QRCode
import com.dpfht.casestudy123.domain.model.QRISTransactionState
import com.dpfht.casestudy123.domain.model.Result

interface PostQRISTransactionUseCase {

  suspend operator fun invoke(qrEntity: QRCode): Result<QRISTransactionState>
}
