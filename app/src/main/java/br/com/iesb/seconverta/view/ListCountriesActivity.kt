package br.com.iesb.seconverta.view

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.iesb.seconverta.R
import br.com.iesb.seconverta.databinding.ActivitySelectCurrencyBinding
import br.com.iesb.seconverta.model.Country
import br.com.iesb.seconverta.model.CurrencyRepository
import br.com.iesb.seconverta.view.adapter.ListCountriesAdapter
import br.com.iesb.seconverta.viewModel.CurrencyViewModel

class ListCountriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectCurrencyBinding
    private lateinit var viewModel: CurrencyViewModel
    private lateinit var countriesAdapter: ListCountriesAdapter
    private var listOfSelectedCountries = arrayListOf<Country>()
    private var listOfCountries = arrayListOf<Country>()
    private var selectedCountry = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_currency)

        viewModel = ViewModelProvider(
            this, CurrencyViewModel.CurrencyViewModelFactory(CurrencyRepository())
        ).get(CurrencyViewModel::class.java)

        if (intent.hasExtra("countries")) {
            listOfSelectedCountries = intent.getSerializableExtra("countries") as ArrayList<Country>
            selectedCountry = intent.getSerializableExtra("currency") as String
        }

        countriesAdapter = ListCountriesAdapter(arrayListOf(), listOfSelectedCountries)

        binding.rvListSelect.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }

        viewModel.getCountries()

        viewModel.countriesListLiveData.observe(this) { countries ->
            listOfCountries.addAll(countries)
            countriesAdapter.update(countries)
        }

        binding.buttonAddSelectedCurrency.setOnClickListener {
            viewModel.listOfCurrenciesFromDb.observe(this) { currencies ->
                currencies.forEach { currency ->
                    if(countriesAdapter.getSelectedCountries().findLast { it.code == currency.code } == null){
                        viewModel.deleteCurrencyItem(currency)
                    }
                }
            }

            countriesAdapter.getSelectedCountries().forEach { country ->
                if (selectedCountry == "-") {
                    viewModel.getCurrency("latest", country.code, country.code)
                } else {
                    viewModel.getCurrency("latest", selectedCountry, country.code)
                }
            }
            finish()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.searchView.clearFocus()
                val filtered = listOfCountries.filter {
                    it.name.uppercase().contains(query.uppercase())
                }
                countriesAdapter.update(filtered)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val filtered = listOfCountries.filter {
                    it.name.uppercase().contains(newText.uppercase())
                }
                countriesAdapter.update(filtered)
                return false
            }
        })
    }
}