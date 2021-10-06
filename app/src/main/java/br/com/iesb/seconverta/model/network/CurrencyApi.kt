package br.com.iesb.seconverta.model.network

import com.google.gson.internal.LinkedTreeMap
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {
    companion object {
        const val BASE_URL = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/"
    }

    @GET("latest/currencies.json")
    suspend fun fetchAllCountries(): Response<LinkedTreeMap<String, String>>

    @GET("{date}/currencies/{country}/{otherCountry}.json")
    suspend fun fetchCurrency(
        @Path("date") date: String,
        @Path("country") country: String,
        @Path("otherCountry") otherCountry: String,
    ): Response<LinkedTreeMap<String, String>>
}