package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import com.dpfht.casestudy123.domain.entity.QRISTransactionState
import com.dpfht.casestudy123.domain.entity.Result

interface PostQRISTransactionUseCase {

  suspend operator fun invoke(qrEntity: QRCodeEntity): Result<QRISTransactionState>
}
