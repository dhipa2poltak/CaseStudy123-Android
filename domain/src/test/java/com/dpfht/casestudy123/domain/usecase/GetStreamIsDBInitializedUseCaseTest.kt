package com.dpfht.casestudy123.domain.usecase

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GetStreamIsDBInitializedUseCaseTest: BaseUseCaseTest() {

  private lateinit var usecase: GetStreamIsDBInitializedUseCase
  private lateinit var compositeDisposable: CompositeDisposable

  @Before
  fun setup() {
    usecase = GetStreamIsDBInitializedUseCaseImpl(appRepository)
    compositeDisposable = CompositeDisposable()
  }

  @After
  fun teardown() {
    compositeDisposable.dispose()
  }

  @Test
  fun `when db is initialized`() {
    val expected = true

    whenever(appRepository.getStreamIsDBInitialized()).thenReturn(Observable.just(expected))

    var actual: Boolean? = null
    val disposable = usecase()
      .subscribe {
        actual = it
      }

    compositeDisposable.add(disposable)

    assertTrue(actual != null)
    assertTrue(expected == actual)
  }

  @Test
  fun `when db is not initialized`() {
    val expected = false

    whenever(appRepository.getStreamIsDBInitialized()).thenReturn(Observable.just(expected))

    var actual: Boolean? = null
    val disposable = usecase()
      .subscribe {
        actual = it
      }

    compositeDisposable.add(disposable)

    assertTrue(actual != null)
    assertTrue(expected == actual)
  }
}
