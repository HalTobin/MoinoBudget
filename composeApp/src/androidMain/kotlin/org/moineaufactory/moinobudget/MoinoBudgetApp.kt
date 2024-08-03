package org.moineaufactory.moinobudget

import android.app.Application
import di.KoinInitializer

class MoinoBudgetApp: Application() {
    override fun onCreate() {
        super.onCreate()
        KoinInitializer(applicationContext).init()
    }
}