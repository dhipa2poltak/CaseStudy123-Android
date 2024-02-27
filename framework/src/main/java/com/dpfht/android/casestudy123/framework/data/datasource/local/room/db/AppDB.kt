package com.dpfht.android.casestudy123.framework.data.datasource.local.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dpfht.android.casestudy123.framework.Constants
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.dao.BalanceDao
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.dao.QRISTransactionDao
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.BalanceDBModel
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.QRISTransactionDBModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(version = 1, entities = [BalanceDBModel::class, QRISTransactionDBModel::class], exportSchema = false)
abstract class AppDB: RoomDatabase() {

  abstract fun balanceDao(): BalanceDao
  abstract fun qrisTransactionDao(): QRISTransactionDao

  private class AppDBCallback(
    private val scope: CoroutineScope
  ) : Callback() {

    private var isPopulatingDB = false

    override fun onCreate(db: SupportSQLiteDatabase) {
      super.onCreate(db)
      INSTANCE?.let { database ->
        scope.launch {
          isPopulatingDB = true
          prePopulateDatabase(database.balanceDao())
          isPopulatingDB = false
          rawIsDBInitialized.onNext(true)
        }
      }
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
      super.onOpen(db)
      if (!isPopulatingDB) {
        rawIsDBInitialized.onNext(true)
      }
    }

    private suspend fun prePopulateDatabase(balanceDao: BalanceDao) {
      val balanceModel = BalanceDBModel(type = "balance", balance = Constants.STARTING_BALANCE)
      balanceDao.insertBalance(balanceModel)
    }
  }

  companion object {
    private var INSTANCE: AppDB? = null

    private val rawIsDBInitialized = BehaviorSubject.createDefault(false)
    val obsIsDBInitialized: Observable<Boolean> = rawIsDBInitialized

    fun getDatabase(context: Context, coroutineScope: CoroutineScope, isInMemory: Boolean = false): AppDB {
      val tempInstance = INSTANCE
      if (tempInstance != null) {
        return tempInstance
      }

      if (isInMemory) {
        return Room
          .inMemoryDatabaseBuilder(context, AppDB::class.java)
          .build()
      }

      synchronized(this) {
        val instance = Room.databaseBuilder(context.applicationContext,
          AppDB::class.java,
          "case_study_123_database")
          .addCallback(AppDBCallback(coroutineScope))
          .build()
        INSTANCE = instance
        return instance
      }
    }
  }
}
