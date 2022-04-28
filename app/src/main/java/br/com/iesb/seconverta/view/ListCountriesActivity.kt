package br.com.iesb.seconverta.view

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.iesb.seconverta.databinding.ActivitySelectCurrencyBinding
import br.com.iesb.seconverta.model.Country
import br.com.iesb.seconverta.utils.Constants
import br.com.iesb.seconverta.view.adapter.ListCountriesAdapter
import br.com.iesb.seconverta.viewModel.CurrencyViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListCountriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectCurrencyBinding
    private val viewModel: CurrencyViewModel by viewModel()
    private lateinit var countriesAdapter: ListCountriesAdapter
    private var listOfSelectedCountries = arrayListOf<Country>()
    private var selectedCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCurrencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.COUNTRIES_KEY) && intent.hasExtra(Constants.CURRENCY_CODE_KEY)) {
            listOfSelectedCountries = intent.getSerializableExtra(Constants.COUNTRIES_KEY) as ArrayList<Country>
            intent.getStringExtra(Constants.CURRENCY_CODE_KEY)?.let {
                selectedCode = it
            }
        }

        countriesAdapter = ListCountriesAdapter(arrayListOf(), listOfSelectedCountries)

        binding.rvListSelect.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }

        viewModel.getCountries()

        viewModel.loading.observe(this) { loading ->
            if (loading) binding.loading.visibility = View.VISIBLE
            else binding.loading.visibility = View.INVISIBLE
        }

        viewModel.countriesListLiveData.observe(this) { countries ->
            countriesAdapter.update(countries)

            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    binding.searchView.clearFocus()
                    val filtered = countries.filter {
                        it.name.uppercase().contains(query.uppercase())
                    }
                    countriesAdapter.update(filtered)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    val filtered = countries.filter {
                        it.name.uppercase().contains(newText.uppercase())
                    }
                    countriesAdapter.update(filtered)
                    return false
                }
            })
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
                viewModel.getCurrency(Constants.UPDATE_LATEST_KEY, selectedCode, country.code)
            }

            viewModel.loading.observe(this) { loading ->
                if(loading) binding.loading.visibility = View.VISIBLE
                else finish()
            }
        }
    }
}