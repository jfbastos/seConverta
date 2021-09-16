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
import java.lang.Exception

class CurrencyViewModel(private val repository: CurrencyRepository) : ViewModel() {
    val countriesListLiveData = MutableLiveData<MutableList<CountryCode>>()
    val currencyList = repository.getCurrencyList()
    val currencyLiveData = MutableLiveData<CurrencyValue>()
    val requestError = MutableLiveData<EventWrapper<String>>()
    private val countries: MutableList<CountryCode> = mutableListOf()


    fun getCountries(date: String) {
        viewModelScope.launch {
            try {
                val response = repository.fetchAllCountries(date)
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        countries.add(CountryCode(it.key, it.value))
                    }
                    countriesListLiveData.value = countries
                } else {
                    requestError.value = EventWrapper("Can't get countries list")
                }
            } catch (e: Exception) {
                requestError.value = EventWrapper("Problem in fetch countries")
            }
        }
    }

    fun getCurrency(date: String, otherCountry : String){
        viewModelScope.launch {
            try{
                val response = repository.fetchCurrency(date, otherCountry)
                if(response.isSuccessful){
                    val returned = response.body()!!
                    currencyLiveData.value = returned
                    insertCurrencyItem(CurrencyItem(otherCountry, returned.brl))
                }
            }catch (e : Exception){
                requestError.value = EventWrapper("Problem to get Currency")
            }
        }
    }

    private fun insertCurrencyItem(currency : CurrencyItem){
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default){
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