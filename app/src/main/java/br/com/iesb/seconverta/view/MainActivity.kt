package br.com.iesb.seconverta.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import br.com.iesb.seconverta.R
import br.com.iesb.seconverta.databinding.ActivityMainBinding
import br.com.iesb.seconverta.model.Country
import br.com.iesb.seconverta.model.Currency
import br.com.iesb.seconverta.model.CurrencyRepository
import br.com.iesb.seconverta.view.adapter.CurrencyAdapter
import br.com.iesb.seconverta.view.adapter.CustomArrayAdapter
import br.com.iesb.seconverta.viewModel.CurrencyViewModel


class MainActivity : AppCompatActivity(), CurrencyAdapter.OnLongItemClickListener {

    private lateinit var viewModel: CurrencyViewModel
    private lateinit var binding: ActivityMainBinding
    private var selectedSpinner: Int = 0
    private val currencyAdapter = CurrencyAdapter(arrayListOf(), this, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(
            this, CurrencyViewModel.CurrencyViewModelFactory(
                CurrencyRepository()
            )
        ).get(CurrencyViewModel::class.java)

        binding.rvList.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
            adapter = currencyAdapter
        }

        viewModel.getCountries()

        viewModel.listOfCurrenciesFromDb.observe(this) {
            currencyAdapter.update(it)
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.listOfCurrenciesFromDb.observe(this) { list ->
            val sortedList = list.sortedBy { it.code }
            println(sortedList)

            val listOfCodes = arrayListOf<String>()

            if (sortedList.isEmpty()) listOfCodes.add("-")
            else sortedList.forEach { currency -> listOfCodes.add(currency.code) }

            CustomArrayAdapter(this, listOfCodes).also { adapter ->
                binding.spinnerCurrency.adapter = adapter
            }

            currencyAdapter.update(sortedList)
            binding.spinnerCurrency.setSelection(selectedSpinner)
        }

        binding.buttonRefreshCurrencies.setOnClickListener {
            selectedSpinner = binding.spinnerCurrency.selectedItemPosition
            viewModel.updateCurrencies(binding.spinnerCurrency.selectedItem.toString())
            Toast.makeText(this, getString(R.string.update_message), Toast.LENGTH_SHORT).show()
        }

        binding.buttonAddCountries.setOnClickListener {

            val countryList = arrayListOf<Country>()
            val intent = Intent(this, ListCountriesActivity::class.java)

            viewModel.listOfCurrenciesFromDb.observe(this) { list ->
                list.forEach { currency ->
                    countryList.add(Country(currency.code, currency.countryName))
                }
            }

            intent.putExtra("countries", countryList)
            intent.putExtra("currency", binding.spinnerCurrency.selectedItem.toString())
            startActivity(intent)
        }

        binding.currencyValue.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER && event.action == KeyEvent.ACTION_UP) {
                viewModel.updateCurrencies(binding.spinnerCurrency.selectedItem.toString())
                selectedSpinner = binding.spinnerCurrency.selectedItemPosition
                return@setOnKeyListener true
            }

            false
        }

        viewModel.requestError.observe(this) { erro ->
            erro.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onLongItemClick(currency: Currency) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_title))
            .setMessage(getString(R.string.delete_message) + currency.code + "?")
            .setPositiveButton(getString(R.string.delete_confirmation)) { _, _ ->
                currencyAdapter.updateDeleted(currency)
                viewModel.deleteCurrencyItem(currency)
            }
            .setNegativeButton(getString(R.string.delete_negation), null)
            .show()
    }
}