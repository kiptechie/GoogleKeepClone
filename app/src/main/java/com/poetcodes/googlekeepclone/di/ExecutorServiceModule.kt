package com.poetcodes.googlekeepclone.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ExecutorServiceModule {

    @Singleton
    @Provides
    fun withSingleThread(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }

    @Singleton
    @Provides
    fun withMultipleThreads(): ExecutorService {
        return Executors.newCachedThreadPool()
    }

}