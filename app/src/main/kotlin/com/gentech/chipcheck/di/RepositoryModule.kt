package com.gentech.chipcheck.di

import com.gentech.chipcheck.data.local.datastore.UserPreferencesRepositoryImpl
import com.gentech.chipcheck.data.repository.CapacityListRepositoryImpl
import com.gentech.chipcheck.data.repository.FirmwareVersionRepositoryImpl
import com.gentech.chipcheck.data.repository.ScanHistoryRepositoryImpl
import com.gentech.chipcheck.data.repository.UnmappedCodeRepositoryImpl
import com.gentech.chipcheck.domain.repository.CapacityListRepository
import com.gentech.chipcheck.domain.repository.FirmwareVersionRepository
import com.gentech.chipcheck.domain.repository.ScanHistoryRepository
import com.gentech.chipcheck.domain.repository.UnmappedCodeRepository
import com.gentech.chipcheck.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindScanHistoryRepository(impl: ScanHistoryRepositoryImpl): ScanHistoryRepository

    @Binds
    @Singleton
    abstract fun bindUnmappedCodeRepository(impl: UnmappedCodeRepositoryImpl): UnmappedCodeRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindFirmwareVersionRepository(impl: FirmwareVersionRepositoryImpl): FirmwareVersionRepository

    @Binds
    @Singleton
    abstract fun bindCapacityListRepository(impl: CapacityListRepositoryImpl): CapacityListRepository
}
