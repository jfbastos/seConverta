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
    val listOfCurrenciesFromDb = repository.getCurrencyListLiveData()
    var loading = MutableLiveData<Boolean>()
    val countriesListLiveData = MutableLiveData<List<Country>>()
    val requestError = MutableLiveData<EventWrapper<String>>()


    fun getCountries() {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repository.fetchAllCountries()
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        CountryCode.addCountrie(it.key, it.value)
                    }
                    countriesListLiveData.value = CountryCode.getCountriesList()
                    loading.value = false
                } else {
                    requestError.value = EventWrapper("Can't get countries list")
                }

            } catch (e: Exception) {
                loading.value = false
                requestError.value = EventWrapper("Problem in fetch countries")
            }
        }
    }

    fun getCurrency(date: String,country : String,otherCountry: String) {

        viewModelScope.launch {
            loading.value = true
            try{
                repository.saveCurrency(date, country, otherCountry)
                loading.value = false
            }catch (e : Exception){
                requestError.value = EventWrapper("Problem to get Currency")

            }
        }
    }

    fun updateCurrencies(country: String) {
        listOfCurrenciesFromDb.value?.forEach {
            getCurrency("latest", country ,it.code)
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