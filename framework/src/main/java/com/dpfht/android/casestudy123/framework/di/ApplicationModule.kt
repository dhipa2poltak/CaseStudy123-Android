package com.dpfht.android.casestudy123.framework.di

import com.dpfht.casestudy123.data.datasource.LocalDataSource
import com.dpfht.casestudy123.data.repository.AppRepositoryImpl
import com.dpfht.casestudy123.domain.repository.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

  @Provides
  @Singleton
  fun provideAppRepository(localDataSource: LocalDataSource): AppRepository {
    return AppRepositoryImpl(localDataSource)
  }
}
