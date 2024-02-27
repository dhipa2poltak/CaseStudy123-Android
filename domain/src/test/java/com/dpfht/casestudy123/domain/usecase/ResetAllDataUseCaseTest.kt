package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.AppException
import com.dpfht.casestudy123.domain.entity.VoidResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class ResetAllDataUseCaseTest: BaseUseCaseTest() {

  private lateinit var resetAllDataUseCase: ResetAllDataUseCase

  @Before
  fun setup() {
    resetAllDataUseCase = ResetAllDataUseCaseImpl(appRepository)
  }

  @Test
  fun `reset all data successfully`() = runTest {
    whenever(appRepository.resetAllData()).then {}

    val expected = VoidResult.Success
    val actual = resetAllDataUseCase()

    assertTrue(expected == actual)
  }

  @Test
  fun `resetting all data is failed`() = runTest {
    val msg = "this is an error message"

    whenever(appRepository.resetAllData()).then {
      throw AppException(msg)
    }

    val expected = VoidResult.Error(msg)
    val actual = resetAllDataUseCase()

    assertTrue(expected == actual)
  }
}
