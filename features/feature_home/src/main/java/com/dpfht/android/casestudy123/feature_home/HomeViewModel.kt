package com.dpfht.android.casestudy123.feature_home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.entity.Result.Error
import com.dpfht.casestudy123.domain.entity.VoidResult
import com.dpfht.casestudy123.domain.usecase.GetBalanceUseCase
import com.dpfht.casestudy123.domain.usecase.ResetAllDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getBalanceUseCase: GetBalanceUseCase,
  private val resetAllDataUseCase: ResetAllDataUseCase
): ViewModel() {

  private val _balance = MutableLiveData<Double>()
  val balance: LiveData<Double> = _balance

  private val _errorMessage = MutableLiveData<String>()
  val errorMessage: LiveData<String> = _errorMessage

  private val _isRefreshing = MutableLiveData<Boolean>()
  val isRefreshing: LiveData<Boolean> = _isRefreshing

  fun start() {
    getBalance()
  }

  private fun getBalance() {
    _isRefreshing.postValue(true)
    viewModelScope.launch {
      when (val result = getBalanceUseCase()) {
        is Result.Success -> {
          onSuccessGetBalance(result.value.balance)
        }
        is Error -> {
          onErrorGetBalance(result.message)
        }
      }
    }
  }

  private fun onSuccessGetBalance(balance: Double) {
    _isRefreshing.postValue(false)
    _balance.postValue(balance)
  }

  private fun onErrorGetBalance(message: String) {
    _isRefreshing.postValue(false)
    _errorMessage.postValue(message)
  }

  fun resetAllData() {
    viewModelScope.launch {
      when (val result = resetAllDataUseCase()) {
        VoidResult.Success -> {
          start()
        }
        is VoidResult.Error -> {
          _errorMessage.postValue(result.message)
        }
        else -> {

        }
      }
    }
  }
}
