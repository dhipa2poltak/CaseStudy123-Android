package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.AppException
import com.dpfht.casestudy123.domain.entity.db_entity.BalanceEntity
import com.dpfht.casestudy123.domain.entity.Result
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
class GetBalanceUseCaseTest: BaseUseCaseTest() {

  private lateinit var getBalanceUseCase: GetBalanceUseCase

  @Before
  fun setup() {
    getBalanceUseCase = GetBalanceUseCaseImpl(appRepository)
  }

  @Test
  fun `get balance successfully`() = runTest {
    val balance = BalanceEntity(1, "", 100000.0)

    whenever(appRepository.getBalance()).thenReturn(balance)

    val expected = Result.Success(balance)
    val actual = getBalanceUseCase()

    assertTrue(expected == actual)
  }

  @Test
  fun `fail to get balance`() = runTest {
    val msg = "error getting balance"

    whenever(appRepository.getBalance()).then {
      throw AppException(msg)
    }

    val expected = Result.Error(msg)
    val actual = getBalanceUseCase()

    assertTrue(expected == actual)
  }
}
