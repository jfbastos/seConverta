package br.com.iesb.seconverta.view

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.iesb.seconverta.R
import br.com.iesb.seconverta.databinding.ActivitySelectCurrencyBinding
import br.com.iesb.seconverta.model.CurrencyRepository
import br.com.iesb.seconverta.view.adapter.ListCountriesAdapter
import br.com.iesb.seconverta.viewModel.CurrencyViewModel

class ListCountriesActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySelectCurrencyBinding
    private lateinit var viewModel: CurrencyViewModel
    private val countriesAdapter = ListCountriesAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_currency)

        viewModel = ViewModelProvider(
            this, CurrencyViewModel.CurrencyViewModelFactory(CurrencyRepository())
        ).get(CurrencyViewModel::class.java)

        binding.rvListSelect.apply{
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }

        viewModel.getCountries("latest")

        viewModel.countriesListLiveData.observe(this) {countries ->
            countriesAdapter.update(countries)
        }

        binding.buttonAddSelectedCurrency.setOnClickListener{
            finishActivity(Activity.RESULT_OK)
        }

    }

}