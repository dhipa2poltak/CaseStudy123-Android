package com.dpfht.android.casestudy123.framework.data.datasource.local.room.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AppDBTest {

  private val testDispatcher = UnconfinedTestDispatcher()

  @get:Rule
  val taskExecutorRule = InstantTaskExecutorRule()

  @Test
  fun `get database instance successfully`() = runTest {
    Dispatchers.setMain(testDispatcher)

    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = AppDB.getDatabase(context, GlobalScope)

    withContext(Dispatchers.IO) {
      db.balanceDao().getBalance("balance")
    }

    advanceUntilIdle()

    var isInit: Boolean? = null
    val disposable = AppDB
      .obsIsDBInitialized
      .subscribe {
        isInit = it
      }

    assertTrue(isInit != null)

    disposable.dispose()
  }
}
