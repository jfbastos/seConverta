package br.com.iesb.seconverta.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.iesb.seconverta.model.*
import br.com.iesb.seconverta.utils.Constants
import kotlinx.coroutines.launch

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
                        CountryCode.addCountry(it.key, it.value)
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

    fun getCurrency(date: String, country: String, otherCountry: String) {
        viewModelScope.launch {
            var dateOfRequest = ""
            var currencyCode = ""
            var currencyValue = 0.0
            var currencyName = ""

            loading.value = true

            try {
                val response = repository.fetchCurrency(date, country, otherCountry)
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
                }

                repository.saveCurrency(
                    Currency(
                        code = currencyCode,
                        value = currencyValue,
                        countryName = currencyName,
                        lastUpdate = dateOfRequest,
                    )
                )

                loading.value = false

            } catch (e: Exception) {
                requestError.value = EventWrapper("Problem to get Currency")
            }
        }
    }

    fun updateCurrencies(country: String) {
        listOfCurrenciesFromDb.value?.forEach {
            getCurrency(Constants.UPDATE_LATEST_KEY, country, it.code)
            println("##### ${it.code} #####")
        }
    }

    fun deleteCurrencyItem(currency: Currency) {
        repository.deleteCurrencyItem(currency)
    }
}