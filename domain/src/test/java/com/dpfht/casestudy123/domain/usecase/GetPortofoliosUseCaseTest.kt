package com.dpfht.casestudy123.domain.usecase

import com.dpfht.casestudy123.domain.entity.AppException
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxChartEntity
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
class GetPortofoliosUseCaseTest: BaseUseCaseTest() {

  private lateinit var getPortofoliosUseCase: GetPortofoliosUseCase

  @Before
  fun setup() {
    getPortofoliosUseCase = GetPortofoliosUseCaseImpl(appRepository)
  }

  @Test
  fun `get portofolios successfully`() = runTest {
    val data1 = TrxChartEntity("test1", listOf())
    val data2 = TrxChartEntity("test2", listOf())
    val data3 = TrxChartEntity("test3", listOf())
    val datas = listOf(data1, data2, data3)

    whenever(appRepository.getPortofolios()).thenReturn(datas)

    val expected = Result.Success(datas)
    val actual = getPortofoliosUseCase()

    assertTrue(expected == actual)
  }

  @Test
  fun `fail to get portofolios`() = runTest {
    val msg = "error getting portofolios"

    whenever(appRepository.getPortofolios()).then {
      throw AppException(msg)
    }

    val expected = Result.Error(msg)
    val actual = getPortofoliosUseCase()

    assertTrue(expected == actual)
  }
}
