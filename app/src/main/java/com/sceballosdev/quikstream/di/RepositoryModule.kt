package com.sceballosdev.quikstream.di

import com.sceballosdev.quikstream.data.repository.StreamRepositoryImpl
import com.sceballosdev.quikstream.domain.repository.StreamRepository
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
    abstract fun bindStreamRepository(
        impl: StreamRepositoryImpl
    ): StreamRepository
}