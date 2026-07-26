package com.gentech.chipcheck.di

import com.gentech.chipcheck.data.repository.ChipRuleRepositoryImpl
import com.gentech.chipcheck.domain.repository.ChipRuleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DecoderModule {

    @Binds
    @Singleton
    abstract fun bindChipRuleRepository(impl: ChipRuleRepositoryImpl): ChipRuleRepository
}
