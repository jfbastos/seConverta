package br.com.iesb.seconverta.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import br.com.iesb.seconverta.MyApplication
import br.com.iesb.seconverta.model.network.CurrencyApi
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

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
        return api.fetchCurrency(date, country, otherCountry)
    }

    fun getCurrencyListLiveData(): LiveData<List<Currency>> {
        return MyApplication.database!!.CurrencyDao().currency
    }

    fun saveCurrency (date: String,country : String,otherCountry: String) {
        var dateOfRequest = ""
        var currencyCode = ""
        var currencyValue = 0.0
        var currencyName = ""

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = fetchCurrency(date, country, otherCountry)
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        if (it.key.equals("date")) {
                            dateOfRequest = it.value
                        } else {
                            currencyCode = it.key
                            currencyValue = it.value.toDouble()
                            currencyName = CountryCode.countries.getValue(it.key)
                        }
                    }
                    println("Banco viewModel")
                    addToDatabase(
                        Currency(
                            currencyCode,
                            currencyValue,
                            dateOfRequest,
                            currencyName
                        )
                    )
                }
            }catch (e : Exception){
                throw e
            }
        }
    }

    private fun addToDatabase(currency: Currency) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                MyApplication.database!!.CurrencyDao().insertCurrency(currency)
            }
        }
    }

}