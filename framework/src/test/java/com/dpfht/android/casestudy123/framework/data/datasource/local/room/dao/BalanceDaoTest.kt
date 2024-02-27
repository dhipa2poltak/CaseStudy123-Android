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
  private val balance1 = BalanceDBModel(1, type, 1000.0)
  private val balance2 = BalanceDBModel(2, type, 1000.0)
  private val balance3 = BalanceDBModel(3, type, 1000.0)
  private val balances = listOf(balance1, balance2, balance3)

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
    balanceDao.insertBalance(balance1)
    balanceDao.insertBalance(balance2)
    balanceDao.insertBalance(balance3)

    val expected = balances
    val actual = withContext(Dispatchers.IO) { balanceDao.getBalance(type) }

    assertTrue(expected == actual)
  }

  @Test
  fun `ensure insertBalance, updateBalance and getBalance methods are called successfully`() = runTest {
    balanceDao.insertBalance(balance1)

    val balance = balance1.copy(balance = 10000.0)
    balanceDao.updateBalance(balance)

    val actual = withContext(Dispatchers.IO) { balanceDao.getBalance(type).firstOrNull() }

    assertTrue(actual != null)
    assertTrue(balance == actual)
  }
}
