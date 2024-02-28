package com.dpfht.android.casestudy123.feature_portofolio

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxChartEntity
import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.usecase.GetPortofoliosUseCase
import com.github.mikephil.charting.data.PieEntry
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
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class PortofolioViewModelTest {

  private val testDispatcher = UnconfinedTestDispatcher()

  @get:Rule
  val taskExecutorRule = InstantTaskExecutorRule()

  private lateinit var viewModel: PortofolioViewModel

  @Mock
  private lateinit var getPortofoliosUseCase: GetPortofoliosUseCase

  @Mock
  private lateinit var pieEntriesObserver: Observer<List<PieEntry>>

  @Mock
  private lateinit var errorMessageObserver: Observer<String>

  @Before
  fun setup() {
    Dispatchers.setMain(testDispatcher)
    viewModel = PortofolioViewModel(getPortofoliosUseCase)
  }

  @Test
  fun `fetch portfolios successfully`() = runTest {
    val trx1 = TrxChartEntity("type1", listOf())
    val trx2 = TrxChartEntity("type2", listOf())
    val trx3 = TrxChartEntity("type3", listOf())
    val trx4 = TrxChartEntity("type4", listOf())
    val listOfTrx = listOf(trx1, trx2, trx3, trx4)

    val result = Result.Success(listOfTrx)

    whenever(getPortofoliosUseCase()).thenReturn(result)

    viewModel.pieEntries.observeForever(pieEntriesObserver)
    viewModel.start()

    verify(pieEntriesObserver).onChanged(any())
    assertTrue(viewModel.trxChartEntities == listOfTrx)
  }

  @Test
  fun `fail to fetch portfolios`() = runTest {
    val msg = "this is an error message"

    whenever(getPortofoliosUseCase()).thenReturn(Result.Error(msg))

    viewModel.errorMessage.observeForever(errorMessageObserver)
    viewModel.start()

    verify(errorMessageObserver).onChanged(eq(msg))
  }
}
