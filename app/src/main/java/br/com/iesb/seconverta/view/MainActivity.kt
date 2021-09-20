package br.com.iesb.seconverta.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
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
import br.com.iesb.seconverta.viewModel.CurrencyViewModel


class MainActivity : AppCompatActivity(), CurrencyAdapter.OnLongItemClickListener {
    companion object {
        const val USER_EXTRA = "user"
    }

    private lateinit var viewModel: CurrencyViewModel
    private lateinit var binding: ActivityMainBinding
    private val currencyAdapter = CurrencyAdapter(arrayListOf(), this, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (intent.hasExtra(USER_EXTRA)) {
            binding.userName.text = intent.getSerializableExtra(USER_EXTRA) as String
        }

        viewModel = ViewModelProvider(
            this, CurrencyViewModel.CurrencyViewModelFactory(
                CurrencyRepository()
            )
        ).get(CurrencyViewModel::class.java)

        binding.rvList.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
            adapter = currencyAdapter
        }

        viewModel.updateCurrencies()

        viewModel.currencyList.observe(this) { it ->
            it.forEach {
                viewModel.countriesSelected.add(Country(it.code, it.countryName))
            }
            currencyAdapter.update(it)
        }

        binding.buttonRefreshCurrencies.setOnClickListener { button ->
            viewModel.updateCurrencies()
            Toast.makeText(this, getString(R.string.update_message), Toast.LENGTH_SHORT).show()
        }

        binding.buttonAddCountries.setOnClickListener {
            val intent = Intent(this, ListCountriesActivity::class.java)
            intent.putExtra("countries", viewModel.countriesSelected)
            startActivity(intent)
        }
    }

    override fun onLongItemClick(currency: Currency) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_title))
            .setMessage(getString(R.string.delete_message) + currency.code)
            .setPositiveButton(getString(R.string.delete_confirmation)) { _, _ ->
                currencyAdapter.updateDeleted(currency)
                viewModel.deleteCurrencyItem(currency)
            }
            .setNegativeButton(getString(R.string.delete_negation), null)
            .show()
    }

}