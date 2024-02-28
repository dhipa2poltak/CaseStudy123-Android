package com.dpfht.android.casestudy123.feature_home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dpfht.casestudy123.domain.entity.db_entity.BalanceEntity
import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.entity.VoidResult
import com.dpfht.casestudy123.domain.usecase.GetBalanceUseCase
import com.dpfht.casestudy123.domain.usecase.ResetAllDataUseCase
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
class HomeViewModelTest {

  private val testDispatcher = UnconfinedTestDispatcher()

  @get:Rule
  val taskExecutorRule = InstantTaskExecutorRule()

  private lateinit var viewModel: HomeViewModel

  @Mock
  private lateinit var getBalanceUseCase: GetBalanceUseCase

  @Mock
  private lateinit var resetAllDataUseCase: ResetAllDataUseCase

  @Mock
  private lateinit var balanceObserver: Observer<Double>

  @Mock
  private lateinit var isRefreshingObserver: Observer<Boolean>

  @Mock
  private lateinit var errorMessageObserver: Observer<String>

  private val msg = "this is an error message"

  @Before
  fun setup() {
    Dispatchers.setMain(testDispatcher)
    viewModel = HomeViewModel(getBalanceUseCase, resetAllDataUseCase)
  }

  @Test
  fun `fetch balance successfully`() = runTest {
    val balanceEntity = BalanceEntity(1, "balance", 32000.0)
    val result = Result.Success(balanceEntity)

    whenever(getBalanceUseCase()).thenReturn(result)

    viewModel.balance.observeForever(balanceObserver)
    viewModel.isRefreshing.observeForever(isRefreshingObserver)
    viewModel.start()

    verify(balanceObserver).onChanged(eq(balanceEntity.balance))
    verify(isRefreshingObserver).onChanged(eq(false))
  }

  @Test
  fun `fail to fetch balance`() = runTest {
    whenever(getBalanceUseCase()).thenReturn(Result.Error(msg))

    viewModel.errorMessage.observeForever(errorMessageObserver)
    viewModel.isRefreshing.observeForever(isRefreshingObserver)
    viewModel.start()

    verify(errorMessageObserver).onChanged(eq(msg))
    verify(isRefreshingObserver).onChanged(eq(false))
  }

  @Test
  fun `reset all data successfully`() = runTest {
    whenever(resetAllDataUseCase()).thenReturn(VoidResult.Success)
    viewModel.resetAllData()
    verify(getBalanceUseCase).invoke()
  }

  @Test
  fun `fail to reset all data`() = runTest {
    whenever(resetAllDataUseCase()).thenReturn(VoidResult.Error(msg))
    viewModel.errorMessage.observeForever(errorMessageObserver)

    viewModel.resetAllData()

    verify(errorMessageObserver).onChanged(eq(msg))
  }
}
