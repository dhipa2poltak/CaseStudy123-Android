package com.dpfht.casestudy123.data.repository

import com.dpfht.casestudy123.data.datasource.LocalDataSource
import com.dpfht.casestudy123.domain.entity.AppException
import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxChartEntity
import com.dpfht.casestudy123.domain.entity.db_entity.BalanceEntity
import com.dpfht.casestudy123.domain.entity.db_entity.QRISTransactionEntity
import com.dpfht.casestudy123.domain.repository.AppRepository
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AppRepositoryTest {

  private lateinit var appRepository: AppRepository

  @Mock
  private lateinit var localDataSource: LocalDataSource

  private val msg = "this is an error message"
  private val qrCodeEntity = QRCodeEntity("source", "idTransaction", "merchantName", 12000.0)
  private val balance = BalanceEntity(1, "type", 100000.0)

  @Before
  fun setup() {
    appRepository = AppRepositoryImpl(localDataSource)
  }

  @Test
  fun `calling getPortofolios method in appRepository is succeeded`() = runTest {
    val data1 = TrxChartEntity("test1", listOf())
    val data2 = TrxChartEntity("test2", listOf())
    val data3 = TrxChartEntity("test3", listOf())
    val datas = listOf(data1, data2, data3)

    whenever(localDataSource.getPortofolios()).thenReturn(datas)

    val actual = appRepository.getPortofolios()

    assertTrue(datas == actual)
  }

  @Test
  fun `calling getPortofolios method in appRepository is failed`() = runTest {
    whenever(localDataSource.getPortofolios()).then {
      throw AppException(msg)
    }

    var actual: String? = null
    try {
      appRepository.getPortofolios()
    } catch (e: AppException) {
      actual = e.message
    }

    assertTrue(actual != null)
    assertTrue(msg == actual)
  }

  @Test
  fun `calling getStreamIsDBInitialized method in appRepository is succeeded`() {
    val obs = Observable.just(true)
    whenever(localDataSource.getStreamIsDBInitialized()).thenReturn(obs)

    val actual = appRepository.getStreamIsDBInitialized()

    assertTrue(obs == actual)
  }

  @Test
  fun `calling getBalance method in appRepository is succeeded`() = runTest {
    whenever(localDataSource.getBalance()).thenReturn(balance)

    val actual = appRepository.getBalance()

    assertTrue(balance == actual)
  }

  @Test
  fun `calling getBalance method in appRepository is failed`() = runTest {
    whenever(localDataSource.getBalance()).then {
      throw AppException(msg)
    }

    var actual: String? = null
    try {
      appRepository.getBalance()
    } catch (e: AppException) {
      actual = e.message
    }

    assertTrue(actual != null)
    assertTrue(msg == actual)
  }

  @Test
  fun `calling postQRISTransaction method in appRepository is succeeded`() = runTest {
    whenever(localDataSource.postQRISTransaction(balance, qrCodeEntity)).then {}

    val isError = try {
      appRepository.postQRISTransaction(balance, qrCodeEntity)
      false
    } catch (_: AppException) {
      true
    }

    assertFalse(isError)
  }

  @Test
  fun `calling postQRISTransaction method in appRepository is failed`() = runTest {
    whenever(localDataSource.postQRISTransaction(balance, qrCodeEntity)).then {
      throw AppException(msg)
    }

    var actual: String? = null
    try {
      appRepository.postQRISTransaction(balance, qrCodeEntity)
    } catch (e: AppException) {
      actual = e.message
    }

    assertTrue(actual != null)
    assertTrue(msg == actual)
  }

  @Test
  fun `calling getAllQRISTransaction method in appRepository is succeeded`() = runTest {
    val entity1 = QRISTransactionEntity(1, "", "", "", 1000.0, null)
    val entity2 = QRISTransactionEntity(2, "", "", "", 2000.0, null)
    val entity3 = QRISTransactionEntity(3, "", "", "", 3000.0, null)
    val listTransactions = listOf(entity1, entity2, entity3)

    whenever(localDataSource.getAllQRISTransaction()).thenReturn(listTransactions)

    val actual = appRepository.getAllQRISTransaction()

    assertTrue(listTransactions == actual)
  }

  @Test
  fun `calling getAllQRISTransaction method in appRepository is failed`() = runTest {
    whenever(localDataSource.getAllQRISTransaction()).then {
      throw AppException(msg)
    }

    var actual: String? = null
    try {
      appRepository.getAllQRISTransaction()
    } catch (e: AppException) {
      actual = e.message
    }

    assertTrue(actual != null)
    assertTrue(msg == actual)
  }

  @Test
  fun `calling resetAllData method in appRepository is succeeded`() = runTest {
    whenever(localDataSource.resetAllData()).then {}

    val isError = try {
      appRepository.resetAllData()
      false
    } catch (_: AppException) {
      true
    }

    assertFalse(isError)
  }

  @Test
  fun `calling resetAllData method in appRepository is failed`() = runTest {
    whenever(localDataSource.resetAllData()).then {
      throw AppException(msg)
    }

    var actual: String? = null
    try {
      appRepository.resetAllData()
    } catch (e: AppException) {
      actual = e.message
    }

    assertTrue(actual != null)
    assertTrue(msg == actual)
  }
}
