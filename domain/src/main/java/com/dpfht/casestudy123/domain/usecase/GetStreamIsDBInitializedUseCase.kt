package com.dpfht.casestudy123.domain.usecase

import io.reactivex.rxjava3.core.Observable

interface GetStreamIsDBInitializedUseCase {

  operator fun invoke(): Observable<Boolean>
}
