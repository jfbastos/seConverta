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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_currency)

        viewModel = ViewModelProvider(
            this, CurrencyViewModel.CurrencyViewModelFactory(CurrencyRepository())
        ).get(CurrencyViewModel::class.java)


        if (intent.hasExtra("countries")) {
            listOfSelectedCountries = intent.getSerializableExtra("countries") as ArrayList<Country>
        }


        countriesAdapter = ListCountriesAdapter(arrayListOf(), listOfSelectedCountries)

        binding.rvListSelect.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }

        viewModel.getCountries("latest")

        viewModel.countriesListLiveData.observe(this) { countries ->
            listOfCountries.addAll(countries)
            countriesAdapter.update(countries)
        }

        binding.buttonAddSelectedCurrency.setOnClickListener {
            countriesAdapter.getSelectedCountries().forEach { country ->
                viewModel.getCurrency("latest", country.code)
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