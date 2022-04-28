package br.com.iesb.seconverta.model

import androidx.lifecycle.LiveData
import br.com.iesb.seconverta.model.dao.CurrencyDao
import br.com.iesb.seconverta.model.network.CurrencyApi
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class CurrencyRepository(private val api: CurrencyApi,  private val currencyDao: CurrencyDao) {

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
        return currencyDao.currency
    }

    fun saveCurrency (currency : Currency){
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default){
                currencyDao.insertCurrency(currency)
            }
        }
    }

    fun deleteCurrencyItem(currency: Currency) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                currencyDao.delete(currency.code)
            }
        }
    }

}