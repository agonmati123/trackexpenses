package com.agonmati.tackexpenses.trackexpenses.application

import android.app.Application
import ninja.sakib.pultusorm.core.PultusORM

/**
 * Created by agonmati on 2/11/18.
 */
class AppController: Application() {
    var appPath: String = ""
    var pultusORM: PultusORM? = null

    override fun onCreate() {
        super.onCreate()
        instance = this

    }

    private object Holder { val INSTANCE = AppController() }

    companion object {
        lateinit var instance:AppController
    }

}

