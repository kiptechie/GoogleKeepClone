package com.poetcodes.googlekeepclone.app

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.poetcodes.googlekeepclone.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import io.paperdb.Paper
import io.sentry.Sentry
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
        AndroidThreeTen.init(this)
        Paper.init(this)
        Sentry.captureMessage("App onCreate ran")
    }

}