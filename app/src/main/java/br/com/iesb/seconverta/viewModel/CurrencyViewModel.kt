package br.com.iesb.seconverta.viewModel

import androidx.lifecycle.*
import br.com.iesb.seconverta.MyApplication
import br.com.iesb.seconverta.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrencyViewModel(private val repository: CurrencyRepository) : ViewModel() {
    val countriesListLiveData = MutableLiveData<List<Country>>()
    var currencyList = repository.getCurrencyList()
    val currencyLiveData = MutableLiveData<CurrencyValue>()
    val requestError = MutableLiveData<EventWrapper<String>>()

    fun getCountries(date: String) {
        viewModelScope.launch {
            try {
                val response = repository.fetchAllCountries(date)
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

    fun getCurrency(date: String, otherCountry: String) {
        viewModelScope.launch {
            try {
                val response = repository.fetchCurrency(date, otherCountry)
                if (response.isSuccessful) {
                    val returned = response.body()!!
                    currencyLiveData.value = returned
                    insertCurrencyItem(CurrencyItem(otherCountry, returned.brl))
                }
            } catch (e: Exception) {
                requestError.value = EventWrapper("Problem to get Currency")
            }
        }
    }

    fun updateCurrencies() {

        currencyList.value?.forEach {
            getCurrency("latest", it.code)
        }
    }

    private fun insertCurrencyItem(currency: CurrencyItem) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                MyApplication.database!!.CurrencyDao().insertCurrency(currency)
            }
        }
    }

    class CurrencyViewModelFactory(private val repository: CurrencyRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            //TO-DO CheckCast
            return CurrencyViewModel(repository) as T
        }
    }
}