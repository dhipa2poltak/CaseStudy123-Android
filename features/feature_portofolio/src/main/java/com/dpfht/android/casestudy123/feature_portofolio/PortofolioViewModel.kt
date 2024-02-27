package com.dpfht.android.casestudy123.feature_portofolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpfht.casestudy123.domain.entity.Result
import com.dpfht.casestudy123.domain.entity.Result.Error
import com.dpfht.casestudy123.domain.entity.asset_entity.TrxChartEntity
import com.dpfht.casestudy123.domain.usecase.GetPortofoliosUseCase
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortofolioViewModel @Inject constructor(
  private val getPortofoliosUseCase: GetPortofoliosUseCase
): ViewModel() {

  private val _pieEntries = MutableLiveData<List<PieEntry>>()
  val pieEntries: LiveData<List<PieEntry>> = _pieEntries

  private val _errorMessage = MutableLiveData<String>()
  val errorMessage: LiveData<String> = _errorMessage

  var trxChartEntities: List<TrxChartEntity>? = null

  fun start() {
    getPortofolios()
  }

  private fun getPortofolios() {
    trxChartEntities = null
    viewModelScope.launch {
      when (val result = getPortofoliosUseCase()) {
        is Result.Success -> {
          onSuccessGetPortofolios(result.value)
        }
        is Error -> {
          onErrorGetPortofolios(result.message)
        }
      }
    }
  }

  private fun onSuccessGetPortofolios(data: List<TrxChartEntity>) {
    trxChartEntities = data
    if (data.isNotEmpty()) {
      val trxChart = data[0]
      val trxPieEntries = arrayListOf<PieEntry>()

      for ((i, trxEntity) in trxChart.data.withIndex()) {
        val percentage: Double = try {
          trxEntity.percentage.toDouble()
        } catch (e: Exception) {
          e.printStackTrace()
          0.0
        }

        val pieEntry = PieEntry(percentage.toFloat(), trxEntity.label, i)
        trxPieEntries.add(pieEntry)
      }

      _pieEntries.postValue(trxPieEntries)
    }
  }

  private fun onErrorGetPortofolios(message: String) {
    _errorMessage.postValue(message)
  }
}
