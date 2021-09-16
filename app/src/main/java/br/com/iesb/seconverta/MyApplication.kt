package br.com.iesb.seconverta

import android.app.Application
import androidx.room.Room
import br.com.iesb.seconverta.model.AppDatabase

class MyApplication : Application() {

    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "currency_db")
            .fallbackToDestructiveMigration()
            .build()
    }
}