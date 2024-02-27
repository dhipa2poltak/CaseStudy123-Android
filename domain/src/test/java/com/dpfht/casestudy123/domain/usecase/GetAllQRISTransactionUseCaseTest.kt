package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.AppException
import com.dpfht.casestudy123.domain.entity.db_entity.QRISTransactionEntity
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
class GetAllQRISTransactionUseCaseTest: BaseUseCaseTest() {

  private lateinit var usecase: GetAllQRISTransactionUseCase

  @Before
  fun setup() {
    usecase = GetAllQRISTransactionUseCaseImpl(appRepository)
  }

  @Test
  fun `Get all QRis Transactions successfully`() = runTest {
    val entity1 = QRISTransactionEntity(1, "", "", "", 1000.0, null)
    val entity2 = QRISTransactionEntity(2, "", "", "", 2000.0, null)
    val entity3 = QRISTransactionEntity(3, "", "", "", 3000.0, null)
    val listTransactions = listOf(entity1, entity2, entity3)

    whenever(appRepository.getAllQRISTransaction()).thenReturn(listTransactions)

    val expected = Result.Success(listTransactions)
    val actual = usecase()

    assertTrue(expected == actual)
  }

  @Test
  fun `fail to get all QRis transactions`() = runTest {
    val msg = "error getting data"

    whenever(appRepository.getAllQRISTransaction()).then {
      throw AppException(msg)
    }

    val expected = Result.Error(msg)
    val actual = usecase()

    assertTrue(expected == actual)
  }
}
