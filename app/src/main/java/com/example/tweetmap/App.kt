package com.example.tweetmap

import android.app.Application
import com.example.tweetmap.utils.PrefsHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class App : Application(){
    override fun onCreate() {
        super.onCreate()
        PrefsHelper.init(applicationContext)
    }
}