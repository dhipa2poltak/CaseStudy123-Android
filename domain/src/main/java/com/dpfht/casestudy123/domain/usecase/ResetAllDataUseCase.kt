package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.model.VoidResult


interface ResetAllDataUseCase {

  suspend operator fun invoke(): VoidResult
}
