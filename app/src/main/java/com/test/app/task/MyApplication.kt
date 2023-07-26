package com.test.app.task

import android.app.Application
import com.test.app.task.data.db.RealmDBAdapter
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication:Application() {
    var realmDBAdapterInstance: RealmDBAdapter? = null
        get() = realmDBAdapterInstance

    override fun onCreate() {
        super.onCreate()

        Realm.init(applicationContext)
        val config = RealmConfiguration.Builder() // data to database on ui thread.
            .allowWritesOnUiThread(true) // below line is to delete realm
            // if migration is needed.
            .deleteRealmIfMigrationNeeded() // at last we are calling a method to build.
            .build()
        Realm.setDefaultConfiguration(config)
        realmDBAdapterInstance = RealmDBAdapter.getInstance(this)
    }
}