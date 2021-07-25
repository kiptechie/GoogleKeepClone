package com.poetcodes.googlekeepclone.di

import com.poetcodes.googlekeepclone.repository.models.MultipleThreads
import com.poetcodes.googlekeepclone.repository.models.SingleThread
import com.poetcodes.googlekeepclone.utils.MyExecutors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ExecutorServiceModule {

    @Singleton
    @Provides
    fun provideExecutors(
        singleThread: SingleThread,
        multipleThreads: MultipleThreads
    ): MyExecutors {
        return MyExecutors(
            singleThread.thread,
            multipleThreads.threads
        )
    }

    @Singleton
    @Provides
    fun provideSingleThread(): SingleThread {
        return SingleThread(Executors.newSingleThreadExecutor())
    }

    @Singleton
    @Provides
    fun provideMultipleThreads(): MultipleThreads {
        return MultipleThreads(Executors.newCachedThreadPool())
    }

}