package br.com.iesb.seconverta.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.iesb.seconverta.model.CountryCode
import br.com.iesb.seconverta.model.CurrencyRepository
import br.com.iesb.seconverta.model.EventWrapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class CurrencyViewModel(private val repository: CurrencyRepository) : ViewModel() {
    val countriesListLiveData = MutableLiveData<MutableList<CountryCode>>()
    val requestError = MutableLiveData<EventWrapper<String>>()
    private val countries: MutableList<CountryCode> = mutableListOf()


    fun getCountries(date: String) {
        viewModelScope.launch {
            val response = repository.fetchAllCountries(date)
            if (response.isSuccessful) {
                response.body()?.forEach {
                    countries.add(CountryCode(it.key, it.value))
                }
                countriesListLiveData.value = countries
            } else {
                requestError.value = EventWrapper("Can't get countries list")
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