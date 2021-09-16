package br.com.iesb.seconverta.model

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

    suspend fun fetchAllCountries(date : String): Response<LinkedTreeMap<String, String>>{
        return api.fetchAllCountries(date)
    }



}