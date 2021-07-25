package com.poetcodes.googlekeepclone.utils

import java.util.concurrent.ExecutorService

class MyExecutors(
    private val singleThread: ExecutorService,
    private val multipleThreads: ExecutorService
) {

    fun withSingleThread(): ExecutorService {
        return singleThread
    }

    fun withMultipleThreads(): ExecutorService {
        return multipleThreads
    }
}