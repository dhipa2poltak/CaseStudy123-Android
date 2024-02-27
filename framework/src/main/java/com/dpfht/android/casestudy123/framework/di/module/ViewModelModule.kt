package com.dpfht.android.casestudy123.framework.di.module

import com.dpfht.casestudy123.domain.entity.db_entity.QRISTransactionEntity
import com.dpfht.casestudy123.domain.repository.AppRepository
import com.dpfht.casestudy123.domain.usecase.GetAllQRISTransactionUseCase
import com.dpfht.casestudy123.domain.usecase.GetAllQRISTransactionUseCaseImpl
import com.dpfht.casestudy123.domain.usecase.GetBalanceUseCase
import com.dpfht.casestudy123.domain.usecase.GetBalanceUseCaseImpl
import com.dpfht.casestudy123.domain.usecase.GetPortofoliosUseCase
import com.dpfht.casestudy123.domain.usecase.GetPortofoliosUseCaseImpl
import com.dpfht.casestudy123.domain.usecase.GetStreamIsDBInitializedUseCase
import com.dpfht.casestudy123.domain.usecase.GetStreamIsDBInitializedUseCaseImpl
import com.dpfht.casestudy123.domain.usecase.PostQRISTransactionUseCase
import com.dpfht.casestudy123.domain.usecase.PostQRISTransactionUseCaseImpl
import com.dpfht.casestudy123.domain.usecase.ResetAllDataUseCase
import com.dpfht.casestudy123.domain.usecase.ResetAllDataUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

  @Provides
  fun provideGetPortofoliosUseCase(appRepository: AppRepository): GetPortofoliosUseCase {
    return GetPortofoliosUseCaseImpl(appRepository)
  }

  @Provides
  fun provideGetStreamIsDBInitializedUseCase(appRepository: AppRepository): GetStreamIsDBInitializedUseCase {
    return GetStreamIsDBInitializedUseCaseImpl(appRepository)
  }

  @Provides
  fun provideGetBalanceUseCase(appRepository: AppRepository): GetBalanceUseCase {
    return GetBalanceUseCaseImpl(appRepository)
  }

  @Provides
  fun providePostQRISTransactionUseCase(appRepository: AppRepository): PostQRISTransactionUseCase {
    return PostQRISTransactionUseCaseImpl(appRepository)
  }

  @Provides
  fun provideGetAllQRISTransactionUseCase(appRepository: AppRepository): GetAllQRISTransactionUseCase {
    return GetAllQRISTransactionUseCaseImpl(appRepository)
  }

  @Provides
  fun provideQRISTransactions(): ArrayList<QRISTransactionEntity> {
    return arrayListOf()
  }

  @Provides
  fun provideResetAllDataUseCase(appRepository: AppRepository): ResetAllDataUseCase {
    return ResetAllDataUseCaseImpl(appRepository)
  }
}
