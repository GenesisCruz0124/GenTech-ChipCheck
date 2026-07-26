package com.gentech.chipcheck.di

import android.content.Context
import androidx.room.Room
import com.gentech.chipcheck.data.local.db.AppDatabase
import com.gentech.chipcheck.data.local.db.ScanHistoryDao
import com.gentech.chipcheck.data.local.db.UnmappedCodeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "chipcheck.db").build()

    @Provides
    fun provideScanHistoryDao(database: AppDatabase): ScanHistoryDao = database.scanHistoryDao()

    @Provides
    fun provideUnmappedCodeDao(database: AppDatabase): UnmappedCodeDao = database.unmappedCodeDao()
}
