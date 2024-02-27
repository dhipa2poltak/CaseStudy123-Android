package com.dpfht.android.casestudy123.framework.data.datasource.local.room.dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.db.AppDB
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.QRISTransactionDBModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class QRISTransactionDaoTest {

  private lateinit var appDb: AppDB
  private lateinit var dao: QRISTransactionDao

  private val dbModel1 = QRISTransactionDBModel(1, "source", "1111", "merchantName1", 10000.0, null)
  private val dbModel2 = QRISTransactionDBModel(2, "source", "2222", "merchantName2", 20000.0, null)
  private val dbModel3 = QRISTransactionDBModel(3, "source", "3333", "merchantName3", 30000.0, null)
  private val dbModels = listOf(dbModel1, dbModel2, dbModel3)

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    appDb = AppDB.getDatabase(context, GlobalScope, true)
    dao = appDb.qrisTransactionDao()
  }

  @After
  fun cleanup() {
    appDb.close()
  }

  @Test
  fun `ensure insertQRISTransaction and getAllQRISTransaction methods are called successfully`() = runTest {
    dao.insertQRISTransaction(dbModel1)
    dao.insertQRISTransaction(dbModel2)
    dao.insertQRISTransaction(dbModel3)

    val expected = dbModels
    val actual = withContext(Dispatchers.IO) { dao.getAllQRISTransaction() }

    assertTrue(expected == actual)
  }

  @Test
  fun `ensure insertQRISTransaction, deleteAllQRISTransaction and getAllQRISTransaction methods are called successfully`() = runTest {
    dao.insertQRISTransaction(dbModel1)
    dao.insertQRISTransaction(dbModel2)
    dao.insertQRISTransaction(dbModel3)

    dao.deleteAllQRISTransaction()
    val actual = withContext(Dispatchers.IO) { dao.getAllQRISTransaction() }

    assertTrue(actual.isEmpty())
  }
}
