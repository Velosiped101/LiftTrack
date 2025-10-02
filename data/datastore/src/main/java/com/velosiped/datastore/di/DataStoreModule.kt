package com.velosiped.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.velosiped.datastore.DataStoreRepository
import com.velosiped.datastore.implementation.DataStoreRepositoryImpl
import com.velosiped.datastore.implementation.appProtoDataStore
import com.velosiped.notes.data.datastore.AppProtoStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {
    @Provides
    @Singleton
    fun provideAppProtoDataStore(@ApplicationContext context: Context): DataStore<AppProtoStore> {
        return context.appProtoDataStore
    }

    @Provides
    @Singleton
    fun provideAppProtoDataStoreRepository(dataStore: DataStore<AppProtoStore>): DataStoreRepository {
        return DataStoreRepositoryImpl(dataStore)
    }
}