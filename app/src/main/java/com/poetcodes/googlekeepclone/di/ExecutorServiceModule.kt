package com.poetcodes.googlekeepclone.di

import android.os.Handler
import android.os.Looper
import com.poetcodes.googlekeepclone.repository.models.MainThread
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
        multipleThreads: MultipleThreads,
        mainThread: MainThread
    ): MyExecutors {
        return MyExecutors(
            singleThread.thread,
            multipleThreads.threads,
            mainThread.handler
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

    @Singleton
    @Provides
    fun provideMainThread(): MainThread {
        return MainThread(Handler(Looper.getMainLooper()))
    }

}