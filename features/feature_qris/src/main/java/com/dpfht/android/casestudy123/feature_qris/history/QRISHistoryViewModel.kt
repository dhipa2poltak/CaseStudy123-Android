package com.dpfht.android.casestudy123.feature_qris.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpfht.android.casestudy123.feature_qris.history.adapter.QRISTransactionHistoryAdapter
import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.entity.Result.Error
import com.dpfht.casestudy123.domain.entity.db_entity.QRISTransactionEntity
import com.dpfht.casestudy123.domain.usecase.GetAllQRISTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QRISHistoryViewModel @Inject constructor(
  private val getAllQRISTransactionUseCase: GetAllQRISTransactionUseCase,
  private val qrisTransactionEntities: ArrayList<QRISTransactionEntity>,
  val adapter: QRISTransactionHistoryAdapter,
  ): ViewModel() {

  private val _isNoData = MutableLiveData<Boolean>()
  val isNoData: LiveData<Boolean> = _isNoData

  private val _errorMessage = MutableLiveData<String>()
  val errorMessage: LiveData<String> = _errorMessage

  init {
    adapter.qrisTransactionEntities = qrisTransactionEntities
  }

  fun start() {
    getAllQRISTransaction()
  }

  private fun getAllQRISTransaction() {
    viewModelScope.launch {
      when (val result = getAllQRISTransactionUseCase()) {
        is Result.Success -> {
          onSuccessGetAllQRISTransaction(result.value)
        }
        is Error -> {
          onErrorGetAllQRISTransaction(result.message)
        }
      }
    }
  }

  private fun onSuccessGetAllQRISTransaction(list: List<QRISTransactionEntity>) {
    qrisTransactionEntities.clear()
    adapter.notifyDataSetChanged()
    qrisTransactionEntities.addAll(list)
    adapter.notifyDataSetChanged()
    _isNoData.postValue(qrisTransactionEntities.isEmpty())
  }

  private fun onErrorGetAllQRISTransaction(message: String) {
    _errorMessage.postValue(message)
    _isNoData.postValue(qrisTransactionEntities.isEmpty())
  }
}
