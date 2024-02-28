package com.dpfht.android.casestudy123.framework.data.datasource.local

import android.content.Context
import android.content.res.AssetManager
import androidx.test.core.app.ApplicationProvider
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.db.AppDB
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.BalanceDBModel
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.QRISTransactionDBModel
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.toDomain
import com.dpfht.casestudy123.data.datasource.LocalDataSource
import com.dpfht.casestudy123.domain.entity.AppException
import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class LocalDataSourceTest {

  private lateinit var localDataSource: LocalDataSource

  private lateinit var context: Context
  private lateinit var assetManager: AssetManager
  private lateinit var appDb: AppDB

  private val balanceModel = BalanceDBModel(1, "balance", 32000.0)

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()
    assetManager = context.assets
    appDb = AppDB.getDatabase(context, GlobalScope, true)

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
  fun `call getBalance method in localDataSource successfully`() = runTest {
    appDb.balanceDao().insertBalance(balanceModel)

    val actual = localDataSource.getBalance()

    assertTrue(balanceModel.toDomain() == actual)
  }

  @Test
  fun `call postQRISTransaction method in localDataSource successfully`() = runTest {
    val nominal = 5000.0

    appDb.balanceDao().insertBalance(balanceModel)

    val balance = balanceModel.toDomain()
    val newBalance = balance.balance - nominal
    val theBalance = balance.copy(balance = newBalance)
    val qrCodeEntity = QRCodeEntity("source", "1111", "merchantName", nominal)

    localDataSource.postQRISTransaction(theBalance, qrCodeEntity)

    val savedBalance = localDataSource.getBalance().balance

    assertTrue(newBalance == savedBalance)
  }

  @Test
  fun `call getAllQRISTransaction method in localDataSource successfully`() = runTest {
    val dbModel1 = QRISTransactionDBModel(1, "source", "1111", "merchantName1", 10000.0, null)
    val dbModel2 = QRISTransactionDBModel(2, "source", "2222", "merchantName2", 20000.0, null)
    val dbModel3 = QRISTransactionDBModel(3, "source", "3333", "merchantName3", 30000.0, null)

    val dao = appDb.qrisTransactionDao()
    dao.insertQRISTransaction(dbModel1)
    dao.insertQRISTransaction(dbModel2)
    dao.insertQRISTransaction(dbModel3)

    val expected = listOf(dbModel1.toDomain(), dbModel2.toDomain(), dbModel3.toDomain())
    val actual = localDataSource.getAllQRISTransaction()

    assertTrue(expected == actual)
  }

  @Test
  fun `call resetAllData method in localDataSource successfully`() = runTest {
    var isError = false

    try {
      localDataSource.resetAllData()
    } catch (e: AppException) {
      isError = true
    }

    assertFalse(isError)
  }
}
