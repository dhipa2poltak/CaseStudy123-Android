package com.dpfht.android.casestudy123.feature_qris.transaction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import com.dpfht.casestudy123.domain.entity.QRISTransactionState
import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.usecase.PostQRISTransactionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class QRISTransactionViewModelTest {

  private val testDispatcher = UnconfinedTestDispatcher()

  @get:Rule
  val taskExecutorRule = InstantTaskExecutorRule()

  private lateinit var viewModel: QRISTransactionViewModel

  @Mock
  private lateinit var postQRISTransactionUseCase: PostQRISTransactionUseCase

  @Mock
  private lateinit var pairObserver: Observer<Pair<Boolean, Double>>

  @Mock
  private lateinit var errorMessageObserver: Observer<String>

  private val nominal = 5000.0
  private val qrCodeEntity = QRCodeEntity("source", "1111", "merchantName", nominal)

  @Before
  fun setup() {
    Dispatchers.setMain(testDispatcher)
    viewModel = QRISTransactionViewModel(postQRISTransactionUseCase)
  }

  @Test
  fun `post QRIS transaction successfully`() = runTest {
    val balance = 32000.0
    val newBalance = balance - nominal
    val result = Result.Success(QRISTransactionState.Success(newBalance))

    whenever(postQRISTransactionUseCase(qrCodeEntity)).thenReturn(result)

    viewModel.transactionState.observeForever(pairObserver)
    viewModel.postQRISTransaction(qrCodeEntity)

    verify(pairObserver).onChanged(eq(Pair(true, balance - nominal)))
  }

  @Test
  fun `post QRIS transaction with not enough balance state`() = runTest {
    val balance = 3000.0
    val result = Result.Success(QRISTransactionState.NotEnoughBalance(balance))

    whenever(postQRISTransactionUseCase(qrCodeEntity)).thenReturn(result)

    viewModel.transactionState.observeForever(pairObserver)
    viewModel.postQRISTransaction(qrCodeEntity)

    verify(pairObserver).onChanged(eq(Pair(false, balance)))
  }

  @Test
  fun `fail to post transaction`() = runTest {
    val msg = "this is an error message"

    whenever(postQRISTransactionUseCase(qrCodeEntity)).thenReturn(Result.Error(msg))

    viewModel.errorMessage.observeForever(errorMessageObserver)
    viewModel.postQRISTransaction(qrCodeEntity)

    verify(errorMessageObserver).onChanged(eq(msg))
  }
}
