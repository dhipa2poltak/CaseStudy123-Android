package com.dpfht.android.casestudy123.feature_splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dpfht.android.casestudy123.framework.commons.test.RxImmediateSchedulerRule
import com.dpfht.casestudy123.domain.usecase.GetStreamIsDBInitializedUseCase
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
class SplashViewModelTest {

  private val testDispatcher = UnconfinedTestDispatcher()

  @get:Rule
  val taskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val schedulers = RxImmediateSchedulerRule()

  private lateinit var viewModel: SplashViewModel

  @Mock
  private lateinit var getStreamIsDBInitializedUseCase: GetStreamIsDBInitializedUseCase

  @Mock
  private lateinit var isInitializationDoneObserver: Observer<Boolean>

  @Before
  fun setup() {
    Dispatchers.setMain(testDispatcher)
    viewModel = SplashViewModel(getStreamIsDBInitializedUseCase)
  }

  @Test
  fun `Initialization is success`() = runTest {
    whenever(getStreamIsDBInitializedUseCase()).thenReturn(Observable.just(true))
    viewModel.isInitializationDone.observeForever(isInitializationDoneObserver)
    viewModel.start()

    advanceUntilIdle()

    verify(isInitializationDoneObserver).onChanged(eq(true))
  }
}
