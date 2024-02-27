package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.repository.AppRepository
import org.mockito.Mock

open class BaseUseCaseTest {

  @Mock
  protected lateinit var appRepository: AppRepository
}
