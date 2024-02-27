package com.dpfht.android.casestudy123.framework.di.module

import android.content.Context
import com.dpfht.android.casestudy123.framework.data.datasource.local.LocalDataSourceImpl
import com.dpfht.android.casestudy123.framework.data.datasource.local.room.db.AppDB
import com.dpfht.casestudy123.data.datasource.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DBModule {

  @Provides
  @Singleton
  fun provideAppDB(@ApplicationContext context: Context): AppDB {
    return AppDB.getDatabase(context, GlobalScope)
  }

  @Provides
  @Singleton
  fun provideLocalDataSource(@ApplicationContext context: Context, appDB: AppDB): LocalDataSource {
    return LocalDataSourceImpl(context, context.assets, appDB)
  }
}
