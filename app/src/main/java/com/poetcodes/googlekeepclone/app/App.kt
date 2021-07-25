package com.poetcodes.googlekeepclone.app

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.poetcodes.googlekeepclone.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import org.threeten.bp.DateTimeUtils
import timber.log.Timber

import timber.log.Timber.DebugTree

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
        AndroidThreeTen.init(this)
    }

}