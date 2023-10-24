package com.dpfht.android.casestudy123.feature_splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpfht.casestudy123.domain.usecase.GetStreamIsDBInitializedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
  private val getStreamIsDBInitializedUseCase: GetStreamIsDBInitializedUseCase
): ViewModel() {

  private val _isInitDBDoneData = MutableLiveData<Boolean>()
  val isInitDBDoneData: LiveData<Boolean> = _isInitDBDoneData

  var isDelayDone = false
  var isInitDBDone = false

  private val compositeDisposable = CompositeDisposable()

  fun start() {
    compositeDisposable.add(
      getStreamIsDBInitializedUseCase()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { isDone ->
          isInitDBDone = isDone
          _isInitDBDoneData.postValue(isInitDBDone)
        }
    )
  }

  fun onDelayDone() {
    isDelayDone = true
  }
}
