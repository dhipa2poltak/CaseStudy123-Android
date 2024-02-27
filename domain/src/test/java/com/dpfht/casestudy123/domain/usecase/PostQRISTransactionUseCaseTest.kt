package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.AppException
import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import com.dpfht.casestudy123.domain.entity.QRISTransactionState
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
class PostQRISTransactionUseCaseTest: BaseUseCaseTest() {

  private lateinit var usecase: PostQRISTransactionUseCase

  private val qrCodeEntity = QRCodeEntity("source", "idTransaction", "merchantName", 12000.0)

  @Before
  fun setup() {
    usecase = PostQRISTransactionUseCaseImpl(appRepository)
  }

  @Test
  fun `QRIS transaction is succeeded`() = runTest {
    val balanceEntity = BalanceEntity(1, "type", 50000.0)

    whenever(appRepository.getBalance()).thenReturn(balanceEntity)

    val newBalance = balanceEntity.balance - qrCodeEntity.nominal
    val expected = Result.Success(QRISTransactionState.Success(newBalance))

    val actual = usecase(qrCodeEntity)

    assertTrue(expected == actual)
  }

  @Test
  fun `QRIS transaction is failed because the balance is zero (not enough)`() = runTest {
    val balanceEntity = BalanceEntity(1, "type", 0.0)

    whenever(appRepository.getBalance()).thenReturn(balanceEntity)

    val theBalance = balanceEntity.balance
    val expected = Result.Success(QRISTransactionState.NotEnoughBalance(theBalance))

    val actual = usecase(qrCodeEntity)

    assertTrue(expected == actual)
  }

  @Test
  fun `QRIS transaction is failed because the balance is not enough`() = runTest {
    val balanceEntity = BalanceEntity(1, "type", 10000.0)

    whenever(appRepository.getBalance()).thenReturn(balanceEntity)

    val theBalance = balanceEntity.balance
    val expected = Result.Success(QRISTransactionState.NotEnoughBalance(theBalance))

    val actual = usecase(qrCodeEntity)

    assertTrue(expected == actual)
  }

  @Test
  fun `QRIS transaction is failed because error (exception) is occurred`() = runTest {
    val msg = "this is an error message"

    whenever(appRepository.getBalance()).then {
      throw AppException(msg)
    }

    val expected = Result.Error(msg)
    val actual = usecase(qrCodeEntity)

    assertTrue(expected == actual)
  }
}
