package br.com.iesb.seconverta.model

import android.util.Log
import androidx.lifecycle.LiveData
import br.com.iesb.seconverta.MyApplication
import br.com.iesb.seconverta.model.network.CurrencyApi
import com.google.gson.internal.LinkedTreeMap
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyRepository {

    private val api: CurrencyApi = Retrofit.Builder()
        .baseUrl(CurrencyApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyApi::class.java)

    suspend fun fetchAllCountries(): Response<LinkedTreeMap<String, String>> {
        return api.fetchAllCountries()
    }

    suspend fun fetchCurrency(
        date: String,
        country: String,
        otherCountry: String
    ): Response<LinkedTreeMap<String, String>> {
        return api.fetchCurrency(date, country ,otherCountry)
    }

    fun getCurrencyListLiveData(): LiveData<List<Currency>> {
        Log.d("DB", "updated")
        return MyApplication.database!!.CurrencyDao().currency
    }

}