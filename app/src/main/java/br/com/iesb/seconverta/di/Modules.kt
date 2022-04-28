package br.com.iesb.seconverta.di

import androidx.room.Room
import br.com.iesb.seconverta.model.dao.AppDatabase
import br.com.iesb.seconverta.model.CurrencyRepository
import br.com.iesb.seconverta.model.network.CurrencyApi
import br.com.iesb.seconverta.viewModel.CurrencyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataBaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "currency_db"
        )
            .build()
    }
    single { get<AppDatabase>().CurrencyDao() }
}

val retrofitModule = module {
    single { provideRetrofit() }
    factory { provideCurrencyApi(get()) }
}


val repositoryModule = module {
    single { CurrencyRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { CurrencyViewModel(get()) }
}


fun provideRetrofit() : Retrofit {
   return Retrofit.Builder()
        .baseUrl(CurrencyApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideCurrencyApi(retrofit : Retrofit) : CurrencyApi = retrofit.create(CurrencyApi::class.java)



