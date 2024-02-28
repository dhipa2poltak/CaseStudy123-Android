package com.dpfht.android.casestudy123.framework.data.datasource.local

import android.content.Context
import android.content.res.AssetManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.dao.BalanceDao
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.db.AppDB
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.BalanceDBModel
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.QRISTransactionDBModel
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.toDomain
import com.dpfht.casestudy123.data.datasource.LocalDataSource
import com.dpfht.casestudy123.domain.entity.AppException
import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class LocalDataSourceTest {

  private val testDispatcher = UnconfinedTestDispatcher()

  @get:Rule
  val taskExecutorRule = InstantTaskExecutorRule()

  private lateinit var localDataSource: LocalDataSource

  private lateinit var context: Context
  private lateinit var assetManager: AssetManager
  private lateinit var appDb: AppDB

  private val balanceModel = BalanceDBModel(1, "balance", 32000.0)
  private val msg = "this is an error message"

  @Before
  fun setup() {
    Dispatchers.setMain(testDispatcher)

    context = ApplicationProvider.getApplicationContext()
    assetManager = context.assets
    appDb = spy(AppDB.getDatabase(context, GlobalScope, true))

    localDataSource = LocalDataSourceImpl(context, assetManager, appDb)
  }

  @After
  fun cleanup() {
    appDb.close()
  }

  @Test
  fun `call getStreamIsDBInitialized method in localDataSource successfully`() = runTest {
    var isInit: Boolean? = null
    val disposable = localDataSource
      .getStreamIsDBInitialized()
      .subscribe {
        isInit = it
      }

    assertTrue(isInit != null)

    disposable.dispose()
  }

  @Test
  fun `call getPortofolios method in localDataSource successfully`() = runTest {
    val expectedCount = 4
    val list = localDataSource.getPortofolios().firstOrNull()?.data

    assertTrue(list != null)
    assertTrue(expectedCount == list?.size)
  }

  @Test
  fun `fail in calling getPortofolios method in localDataSource`() = runTest {
    context = mock()
    assetManager = mock()

    localDataSource = LocalDataSourceImpl(context, assetManager, appDb)

    whenever(assetManager.open(anyString())).then {
      throw Exception(msg)
    }

    var actual: String? = null
    try {
      localDataSource.getPortofolios()
    } catch (e: AppException) {
      actual = e.message
    }

    assertTrue(actual != null)
    assertTrue(msg == actual)
  }

  @Test
  fun `call getBalance method in localDataSource successfully`() = runTest {
    withContext(Dispatchers.IO) {
      appDb.balanceDao().insertBalance(balanceModel)
    }

    val actual = localDataSource.getBalance()

    assertTrue(balanceModel.toDomain() == actual)
  }

  @Test
  fun `fail in calling getBalance method in localDataSource`() = runTest {
    whenever(appDb.balanceDao()).then {
      throw Exception()
    }

    var actual: String? = null
    try {
      localDataSource.getBalance()
    } catch (e: AppException) {
      actual = e.message
    }

    assertTrue(actual != null)
    assertTrue(actual?.isNotEmpty() == true)
  }

  @Test
  fun `call postQRISTransaction method in localDataSource successfully`() = runTest {
    val nominal = 5000.0

    withContext(Dispatchers.IO) {
      appDb.balanceDao().insertBalance(balanceModel)
    }

    val balance = balanceModel.toDomain()
    val newBalance = balance.balance - nominal
    val theBalance = balance.copy(balance = newBalance)
    val qrCodeEntity = QRCodeEntity("source", "1111", "merchantName", nominal)

    localDataSource.postQRISTransaction(theBalance, qrCodeEntity)

    val savedBalance = localDataSource.getBalance().balance

    assertTrue(newBalance == savedBalance)
  }

  @Test
  fun `fail in calling postQRISTransaction method in localDataSource`() = runTest {
    var actual: String? = null
    try {
      val qrCodeEntity = QRCodeEntity("source", "1111", "merchantName", 5000.0)
      localDataSource.postQRISTransaction(balanceModel.toDomain(), qrCodeEntity)
    } catch (e: AppException) {
      actual = e.message
    }

    assertTrue(actual != null)
    assertTrue(actual?.isNotEmpty() == true)
  }

  @Test
  fun `call getAllQRISTransaction method in localDataSource successfully`() = runTest {
    val dbModel1 = QRISTransactionDBModel(1, "source", "1111", "merchantName1", 10000.0, null)
    val dbModel2 = QRISTransactionDBModel(2, "source", "2222", "merchantName2", 20000.0, null)
    val dbModel3 = QRISTransactionDBModel(3, "source", "3333", "merchantName3", 30000.0, null)

    withContext(Dispatchers.IO) {
      val dao = appDb.qrisTransactionDao()
      dao.insertQRISTransaction(dbModel1)
      dao.insertQRISTransaction(dbModel2)
      dao.insertQRISTransaction(dbModel3)
    }

    val expected = listOf(dbModel1.toDomain(), dbModel2.toDomain(), dbModel3.toDomain())
    val actual = localDataSource.getAllQRISTransaction()

    assertTrue(expected == actual)
  }

  @Test
  fun `fail in calling getAllQRISTransaction method in localDataSource`() = runTest {
    whenever(appDb.qrisTransactionDao()).then {
      throw Exception()
    }

    var actual: String? = null
    try {
      localDataSource.getAllQRISTransaction()
    } catch (e: AppException) {
      actual = e.message
    }

    assertTrue(actual != null)
    assertTrue(actual?.isNotEmpty() == true)
  }

  @Test
  fun `call resetAllData method in localDataSource successfully`() = runTest {
    var isError = false

    withContext(Dispatchers.IO) {
      appDb.balanceDao().insertBalance(balanceModel)
    }

    try {
      localDataSource.resetAllData()
    } catch (e: AppException) {
      isError = true
    }

    assertFalse(isError)
  }

  @Test
  fun `fail in calling resetAllData method in localDataSource`() = runTest {
    val balanceDao: BalanceDao = mock()
    whenever(appDb.balanceDao()).thenReturn(balanceDao)
    whenever(balanceDao.getBalance("balance")).thenReturn(listOf(balanceModel))
    whenever(balanceDao.updateBalance(any())).thenReturn(0)

    var actual: String? = null
    try {
      localDataSource.resetAllData()
    } catch (e: AppException) {
      actual = e.message
    }

    assertTrue(actual != null)
    assertTrue(actual?.isNotEmpty() == true)
  }
}
