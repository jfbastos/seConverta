package br.com.iesb.seconverta.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.google.android.material.textview.MaterialTextView


class MainActivity : AppCompatActivity(), CurrencyAdapter.OnLongItemClickListener {

    private val countryList = arrayListOf<Country>()
    private lateinit var viewModel: CurrencyViewModel
    private lateinit var binding: ActivityMainBinding
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

        binding.buttonRefreshCurrencies.setOnClickListener {
            viewModel.updateCurrencies(binding.spinnerCurrency.selectedItem.toString())
            updateList()
            Toast.makeText(this, getString(R.string.update_message), Toast.LENGTH_SHORT).show()
        }

        viewModel.listOfCurrenciesFromDb.observe(this) { list ->
            val listOfCodes = arrayListOf<String>()

            if(list.isEmpty()){
                listOfCodes.add("-")
            }else{
                list.forEach { currency ->
                    listOfCodes.add(currency.code)
                }
            }

            CustomArrayAdapter(this, listOfCodes).also { adapter ->
                binding.spinnerCurrency.adapter = adapter
            }
        }

        binding.buttonAddCountries.setOnClickListener {

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
                updateList()
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

    override fun onResume() {
        super.onResume()
        updateList()

    }

    private fun updateList() {
        viewModel.listOfCurrenciesFromDb.observe(this) {
            currencyAdapter.update(it)
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