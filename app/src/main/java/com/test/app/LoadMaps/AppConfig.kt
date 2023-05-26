package com.test.app.LoadMaps

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration


//import io.realm.mongodb.App
//import io.realm.mongodb.AppConfiguration

//lateinit var taskApp: App
var Appid = "colorseteditor-hocos"

class AppConfig: Application() {
    override fun onCreate() {
        super.onCreate()

        // on below line we are
        // initializing our realm database.
        // on below line we are
        // initializing our realm database.
        Realm.init(this)

        // on below line we are setting realm configuration

        // on below line we are setting realm configuration
        val config = RealmConfiguration.Builder() // below line is to allow write
            // data to database on ui thread.
            .allowWritesOnUiThread(true) // below line is to delete realm
            // if migration is needed.
            .deleteRealmIfMigrationNeeded() // at last we are calling a method to build.
            .build()
        // on below line we are setting
        // configuration to our realm database.
        // on below line we are setting
        // configuration to our realm database.
        Realm.setDefaultConfiguration(config)

    }
}