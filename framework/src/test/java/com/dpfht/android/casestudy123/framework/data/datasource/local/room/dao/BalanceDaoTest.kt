package com.dpfht.android.casestudy123.framework.data.datasource.local.room.dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.db.AppDB
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.BalanceDBModel
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
class BalanceDaoTest {

  private lateinit var appDb: AppDB
  private lateinit var balanceDao: BalanceDao

  private val type = "type"
  private val balance = BalanceDBModel(1, type, 32000.0)

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    appDb = AppDB.getDatabase(context, GlobalScope, true)
    balanceDao = appDb.balanceDao()
  }

  @After
  fun cleanup() {
    appDb.close()
  }

  @Test
  fun `ensure insertBalance and getBalance methods are called successfully`() = runTest {
    balanceDao.insertBalance(balance)

    val actual = withContext(Dispatchers.IO) { balanceDao.getBalance(type).firstOrNull() }

    assertTrue(actual != null)
    assertTrue(balance == actual)
  }

  @Test
  fun `ensure insertBalance, updateBalance and getBalance methods are called successfully`() = runTest {
    balanceDao.insertBalance(balance)

    val theBalance = balance.copy(balance = 10000.0)
    balanceDao.updateBalance(theBalance)

    val actual = withContext(Dispatchers.IO) { balanceDao.getBalance(type).firstOrNull() }

    assertTrue(actual != null)
    assertTrue(theBalance == actual)
  }
}
