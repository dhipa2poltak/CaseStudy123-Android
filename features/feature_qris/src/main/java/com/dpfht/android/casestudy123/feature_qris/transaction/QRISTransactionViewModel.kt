package com.dpfht.android.casestudy123.feature_qris.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.entity.QRCodeEntity
import com.dpfht.casestudy123.domain.entity.QRISTransactionState
import com.dpfht.casestudy123.domain.usecase.PostQRISTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QRISTransactionViewModel @Inject constructor(
  private val postQRISTransactionUseCase: PostQRISTransactionUseCase
): ViewModel() {

  private val _transactionState = MutableLiveData<Pair<Boolean, Double>>()
  val transactionState: LiveData<Pair<Boolean, Double>> = _transactionState

  private val _errorMessage = MutableLiveData<String>()
  val errorMessage: LiveData<String> = _errorMessage

  var isDone = false

  fun postQRISTransaction(entity: QRCodeEntity) {
    viewModelScope.launch {
      when (val result = postQRISTransactionUseCase(entity)) {
        is Result.Success -> {
          onSuccessPostQRISTransaction(result.value)
        }
        is Result.ErrorResult -> {
          onErrorPostQRISTransaction(result.message)
        }
      }
    }
  }

  private fun onSuccessPostQRISTransaction(state: QRISTransactionState) {
    when (state) {
      is QRISTransactionState.Success -> {
        isDone = true
        _transactionState.postValue(Pair(true, state.balance))
      }
      is QRISTransactionState.NotEnoughBalance -> {
        _transactionState.postValue(Pair(false, state.balance))
      } else -> {

      }
    }
  }

  private fun onErrorPostQRISTransaction(message: String) {
    _errorMessage.postValue(message)
  }
}
