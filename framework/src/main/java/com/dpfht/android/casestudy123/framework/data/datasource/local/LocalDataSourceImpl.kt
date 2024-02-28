package com.dpfht.android.casestudy123.framework.data.datasource.local

import android.content.Context
import android.content.res.AssetManager
import com.dpfht.android.casestudy123.framework.Constants
import com.dpfht.android.casestudy123.framework.R
import com.dpfht.android.casestudy123.framework.data.datasource.local.assets.model.portofolio.TrxChartAssetModel
import com.dpfht.android.casestudy123.framework.data.datasource.local.assets.model.portofolio.toDomain
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.db.AppDB
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.BalanceDBModel
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.QRISTransactionDBModel
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.model.toDomain
import com.dpfht.casestudy123.data.datasource.LocalDataSource
import com.dpfht.casestudy123.domain.entity.AppException
import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxChartEntity
import com.dpfht.casestudy123.domain.entity.db_entity.BalanceEntity
import com.dpfht.casestudy123.domain.entity.db_entity.QRISTransactionEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Calendar

class LocalDataSourceImpl(
  private val context: Context,
  private val assetManager: AssetManager,
  private val appDB: AppDB
): LocalDataSource {

  init {
    try {
      GlobalScope.launch(Dispatchers.IO) {
        appDB.balanceDao().getBalance("balance")
      }
    } catch (_: Exception) {}
  }

  override fun getStreamIsDBInitialized(): Observable<Boolean> {
    return AppDB.obsIsDBInitialized
  }

  override suspend fun getPortofolios(): List<TrxChartEntity> {
    return withContext(Dispatchers.IO) {
      var text = ""

      var reader: BufferedReader? = null
      try {
        reader = BufferedReader(InputStreamReader(assetManager.open("portofolio.json")))

        var mLine = reader.readLine()
        while (mLine != null) {
          text += mLine
          mLine = reader.readLine()
        }
      } catch (e: Exception) {
        e.printStackTrace()
        throw AppException(e.message ?: context.getString(R.string.framework_text_error_reading_file))
      } finally {
        if (reader != null) {
          try {
            reader.close()
          } catch (_: IOException) {}
        }
      }

      val typeTokenCity = object : TypeToken<List<TrxChartAssetModel>>() {}.type
      val trxChartAsset = Gson().fromJson<List<TrxChartAssetModel>>(text, typeTokenCity)

      trxChartAsset.map { it.toDomain() }
    }
  }

  override suspend fun getBalance(): BalanceEntity {
    try {
      val entity = withContext(Dispatchers.IO) {
        val list = appDB.balanceDao().getBalance("balance").map { it.toDomain() }
        list.firstOrNull()
      }

      return entity ?: throw Exception(context.getString(R.string.framework_text_error_no_balance_found))
    } catch (e: Exception) {
      e.printStackTrace()
      throw AppException(context.getString(R.string.framework_text_failed_get_balance))
    }
  }

  override suspend fun postQRISTransaction(balanceEntity: BalanceEntity, qrEntity: QRCodeEntity) {
    try {
      withContext(Dispatchers.IO) {
        appDB.beginTransaction()

        try {
          val newBalanceModel = BalanceDBModel(id = balanceEntity.id, type = balanceEntity.type, balance = balanceEntity.balance)
          val count = appDB.balanceDao().updateBalance(newBalanceModel)

          if (count > 0) {
            val newQRISTransaction = QRISTransactionDBModel(
              source = qrEntity.source,
              idTransaction = qrEntity.idTransaction,
              merchantName = qrEntity.merchantName,
              nominal = qrEntity.nominal,
              transactionDateTime = Calendar.getInstance().time
            )
            appDB.qrisTransactionDao().insertQRISTransaction(newQRISTransaction)

            appDB.setTransactionSuccessful()
          } else {
            throw Exception(context.getString(R.string.framework_text_failed_update_balance))
          }
        } catch (e: Exception) {
          e.printStackTrace()
          throw Exception(context.getString(R.string.framework_text_failed_post_transaction))
        } finally {
          appDB.endTransaction()
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
      throw AppException(context.getString(R.string.framework_text_failed_post_transaction))
    }
  }

  override suspend fun getAllQRISTransaction(): List<QRISTransactionEntity> {
    return try {
      val entities = withContext(Dispatchers.IO) {
        appDB.qrisTransactionDao().getAllQRISTransaction().map { it.toDomain() }
      }

      entities
    } catch (e: Exception) {
      e.printStackTrace()
      throw AppException(context.getString(R.string.framework_text_failed_get_transaction))
    }
  }

  override suspend fun resetAllData() {
    try {
      withContext(Dispatchers.IO) {
        appDB.beginTransaction()

        try {
          val balanceModel = appDB.balanceDao().getBalance("balance").firstOrNull()
          balanceModel?.let {
            val newBalanceModel = balanceModel.copy(balance = Constants.STARTING_BALANCE)
            val count = appDB.balanceDao().updateBalance(newBalanceModel)

            if (count > 0) {
              appDB.qrisTransactionDao().deleteAllQRISTransaction()
              appDB.setTransactionSuccessful()
            } else {
              throw Exception(context.getString(R.string.framework_text_failed_reset_balance))
            }
          }
        } catch (e: Exception) {
          e.printStackTrace()
          throw Exception(context.getString(R.string.framework_text_failed_reset_data))
        } finally {
          appDB.endTransaction()
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
      throw AppException(context.getString(R.string.framework_text_failed_reset_data))
    }
  }
}
