package br.com.iesb.seconverta.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.iesb.seconverta.MyApplication
import br.com.iesb.seconverta.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrencyViewModel(private val repository: CurrencyRepository) : ViewModel() {
    val listOfCurrenciesFromDb = repository.getCurrencyListLiveData()

    val countriesListLiveData = MutableLiveData<List<Country>>()
    val requestError = MutableLiveData<EventWrapper<String>>()


    fun getCountries() {
        viewModelScope.launch {
            try {
                val response = repository.fetchAllCountries()
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        CountryCode.addCountrie(it.key, it.value)
                    }
                    countriesListLiveData.value = CountryCode.getCountriesList()
                } else {
                    requestError.value = EventWrapper("Can't get countries list")
                }
            } catch (e: Exception) {
                requestError.value = EventWrapper("Problem in fetch countries")
            }
        }
    }

    fun getCurrency(date: String,country : String,otherCountry: String) {
        var dateOfRequest = ""
        var currencyCode = ""
        var currencyValue = 0.0
        var currencyName = ""
        var savedCurrency: Currency

        viewModelScope.launch {
            try {
                val response = repository.fetchCurrency(date, country, otherCountry)
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        if (it.key.equals("date")) {
                            dateOfRequest = it.key
                            //Log.d("Currency", it.value)
                        } else {
                            currencyCode = it.key
                            currencyValue = try {
                                it.value.toDouble()
                            } catch (e: Exception) {
                                0.0
                            }
                            try {
                                currencyName = CountryCode.countries.getValue(it.key)
                            } catch (e: Exception) {
                                requestError.value =
                                    EventWrapper("Error on find name")
                            }
                            //Log.d("Currency", "${it.key} | ${it.value} | $currencyName")
                        }
                    }
                    savedCurrency =
                        Currency(currencyCode, currencyValue, dateOfRequest, currencyName)
                    addToDatabase(savedCurrency)
                    println("Request in APi")
                }
            } catch (e: Exception) {
                requestError.value = EventWrapper("Problem to get Currency")
            }
        }
    }


    fun updateCurrencies(country: String) {
        listOfCurrenciesFromDb.value?.forEach {
            getCurrency("latest", country ,it.code)
        }
    }

    private fun addToDatabase(currency: Currency) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                MyApplication.database!!.CurrencyDao().insertCurrency(currency)
            }
        }
    }

    fun deleteCurrencyItem(currency: Currency) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                MyApplication.database!!.CurrencyDao().delete(currency.code)
            }
        }
    }

    class CurrencyViewModelFactory(private val repository: CurrencyRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CurrencyViewModel(repository) as T
        }
    }
}