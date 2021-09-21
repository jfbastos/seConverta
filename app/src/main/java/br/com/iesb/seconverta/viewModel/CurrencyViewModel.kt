package br.com.iesb.seconverta.viewModel

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
    val countriesListLiveData = MutableLiveData<List<Country>>()
    var currencyList = repository.getCurrencyList()
    val currencyLiveData = MutableLiveData<CurrencyValue>()
    val requestError = MutableLiveData<EventWrapper<String>>()
    val countriesSelected = arrayListOf<Country>()

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
                    insertCurrencyItem(
                        Currency(
                            otherCountry,
                            returned.brl,
                            returned.date,
                            CountryCode.getCountryName(otherCountry),
                            true
                        )
                    )
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

    private fun insertCurrencyItem(currency: Currency) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                MyApplication.database!!.CurrencyDao().insertCurrency(currency)
            }
        }
    }

    fun deleteCurrencyItem(currency: Currency) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                countriesSelected.removeAt(findIndexOf(currency.code))
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

    private fun findIndexOf(code : String) : Int{
       return countriesSelected.indexOfFirst { it.code==code }
    }
}