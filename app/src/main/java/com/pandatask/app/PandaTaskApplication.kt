package com.pandatask.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Panda Task
 * Enables Hilt dependency injection
 */
@HiltAndroidApp
class PandaTaskApplication : Application()