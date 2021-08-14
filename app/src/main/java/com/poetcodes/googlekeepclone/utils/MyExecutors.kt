package com.poetcodes.googlekeepclone.utils

import android.os.Handler
import java.util.concurrent.ExecutorService

class MyExecutors(
    private val singleThread: ExecutorService,
    private val multipleThreads: ExecutorService,
    private val handler: Handler
) {

    fun withSingleThread(): ExecutorService {
        return singleThread
    }

    fun withMultipleThreads(): ExecutorService {
        return multipleThreads
    }

    fun withHandler(): Handler {
        return handler
    }
}