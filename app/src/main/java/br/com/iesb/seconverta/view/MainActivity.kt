package br.com.iesb.seconverta.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import br.com.iesb.seconverta.R
import br.com.iesb.seconverta.databinding.ActivityMainBinding
import br.com.iesb.seconverta.model.Country
import br.com.iesb.seconverta.model.Currency
import br.com.iesb.seconverta.utils.Constants
import br.com.iesb.seconverta.view.adapter.CurrencyAdapter
import br.com.iesb.seconverta.view.adapter.CustomArrayAdapter
import br.com.iesb.seconverta.viewModel.CurrencyViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    private val viewModel: CurrencyViewModel by viewModel()

    private var selectedSpinner: Int = 0
    private lateinit var currencyAdapter: CurrencyAdapter
    private val listOfCodes = arrayListOf(Constants.BASE_CURRENCY)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getCountries()

        currencyAdapter = CurrencyAdapter(binding.currencyValue)

        setupRecycler()

        viewModel.listOfCurrenciesFromDb.observe(this) { list ->

            spinnerImplementation(list)

            currencyAdapter.differ.submitList(list.sortedBy { it.code })
            currencyAdapter.notifyDataSetChanged()

        }

        binding.spinnerCurrency.setSelection(selectedSpinner)

        doRefresh()

        addCurrency()

        currencyAdapter.setOnLongItemClickListener { currency ->
            confirmDelete(currency)
            return@setOnLongItemClickListener true
        }

        binding.currencyValue.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER && event.action == KeyEvent.ACTION_UP) {
                viewModel.updateCurrencies(binding.spinnerCurrency.selectedItem.toString())
                return@setOnKeyListener true
            }
            false
        }

        viewModel.requestError.observe(this) { error ->
            error.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addCurrency() {
        binding.buttonAddCountries.setOnClickListener {
            val countryList = arrayListOf<Country>()
            val intent = Intent(this, ListCountriesActivity::class.java)

            currencyAdapter.differ.currentList.forEach {
                countryList.add(Country(it.code, it.countryName))
            }

            selectedSpinner = binding.spinnerCurrency.selectedItemPosition

            intent.putExtra(Constants.COUNTRIES_KEY, countryList)
            intent.putExtra(
                Constants.CURRENCY_CODE_KEY,
                binding.spinnerCurrency.selectedItem.toString()
            )
            startActivity(intent)
        }
    }

    private fun doRefresh() {
        binding.buttonRefreshCurrencies.setOnClickListener {
            selectedSpinner = binding.spinnerCurrency.selectedItemPosition
            viewModel.updateCurrencies(binding.spinnerCurrency.selectedItem.toString())
            Toast.makeText(this, getString(R.string.update_message), Toast.LENGTH_SHORT).show()
        }
    }

    private fun spinnerImplementation(list: List<Currency>) {
        list.forEach { currency ->
            if (listOfCodes.contains(currency.code).not()) listOfCodes.add(currency.code)
        }

        val deletedCodes = arrayListOf<Int>()
        listOfCodes.forEach { spinnerCode ->
            if (list.find { it.code == spinnerCode } == null && spinnerCode != Constants.BASE_CURRENCY) deletedCodes.add(
                listOfCodes.indexOf(spinnerCode)
            )
        }

        deletedCodes.forEach {
            listOfCodes.removeAt(it)
        }

        CustomArrayAdapter(this, listOfCodes).also { adapter ->
            binding.spinnerCurrency.adapter = adapter
        }
    }

    private fun confirmDelete(currency: Currency) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_title))
            .setMessage(getString(R.string.delete_message) + currency.code + "?")
            .setPositiveButton(getString(R.string.delete_confirmation)) { _, _ ->
                selectedSpinner = 0
                listOfCodes.remove(currency.code)
                viewModel.deleteCurrencyItem(currency)
            }
            .setNegativeButton(getString(R.string.delete_negation), null)
            .show()
    }

    private fun setupRecycler() {
        binding.rvList.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
            adapter = currencyAdapter
        }
    }
}