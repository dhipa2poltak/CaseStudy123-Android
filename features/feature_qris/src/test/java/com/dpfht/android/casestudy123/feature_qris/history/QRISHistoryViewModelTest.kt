package com.dpfht.android.casestudy123.feature_qris.history

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dpfht.android.casestudy123.feature_qris.history.adapter.QRISTransactionHistoryAdapter
import com.dpfht.casestudy123.domain.entity.db_entity.QRISTransactionEntity
import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.usecase.GetAllQRISTransactionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
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
class QRISHistoryViewModelTest {

  private val testDispatcher = UnconfinedTestDispatcher()

  @get:Rule
  val taskExecutorRule = InstantTaskExecutorRule()

  private lateinit var viewModel: QRISHistoryViewModel

  @Mock
  private lateinit var getAllQRISTransactionUseCase: GetAllQRISTransactionUseCase

  @Mock
  private lateinit var adapter: QRISTransactionHistoryAdapter

  @Mock
  private lateinit var isNoDataObserver: Observer<Boolean>

  @Mock
  private lateinit var errorMessageObserver: Observer<String>

  private val qrisTransactionEntities = ArrayList<QRISTransactionEntity>()

  @Before
  fun setup() {
    Dispatchers.setMain(testDispatcher)
    viewModel = QRISHistoryViewModel(getAllQRISTransactionUseCase, qrisTransactionEntities, adapter)
  }

  @Test
  fun `fetch QRIS transactions successfully`() = runTest {
    val qrisTransaction1 = QRISTransactionEntity(1, "source", "1111", "name1", 1000.0, null)
    val qrisTransaction2 = QRISTransactionEntity(2, "source", "2222", "name2", 2000.0, null)
    val qrisTransaction3 = QRISTransactionEntity(3, "source", "3333", "name3", 3000.0, null)
    val listTransaction = listOf(qrisTransaction1, qrisTransaction2, qrisTransaction3)

    val result = Result.Success(listTransaction)

    whenever(getAllQRISTransactionUseCase()).thenReturn(result)

    viewModel.isNoData.observeForever(isNoDataObserver)
    viewModel.start()

    assertTrue(qrisTransactionEntities.toList() == listTransaction)
    verify(isNoDataObserver).onChanged(eq(false))
  }

  @Test
  fun `fail to fetch QRIS transactions`() = runTest {
    val msg = "this is an error message"

    whenever(getAllQRISTransactionUseCase()).thenReturn(Result.Error(msg))

    viewModel.errorMessage.observeForever(errorMessageObserver)
    viewModel.isNoData.observeForever(isNoDataObserver)
    viewModel.start()

    verify(errorMessageObserver).onChanged(eq(msg))
    verify(isNoDataObserver).onChanged(eq(true))
  }
}
