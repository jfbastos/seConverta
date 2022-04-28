package br.com.iesb.seconverta.di

import android.app.Application
import br.com.iesb.seconverta.di.dataBaseModule
import br.com.iesb.seconverta.di.repositoryModule
import br.com.iesb.seconverta.di.retrofitModule
import br.com.iesb.seconverta.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(listOf(retrofitModule,dataBaseModule, repositoryModule, viewModelModule))
        }
    }
}