package br.com.iesb.seconverta.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import br.com.iesb.seconverta.R
import br.com.iesb.seconverta.databinding.ActivityMainBinding
import br.com.iesb.seconverta.model.CurrencyRepository
import br.com.iesb.seconverta.model.User
import br.com.iesb.seconverta.view.adapter.CurrencyAdapter
import br.com.iesb.seconverta.viewModel.CurrencyViewModel

class MainActivity : AppCompatActivity() {
    companion object {
        val USER_EXTRA = "user"
    }

    private lateinit var viewModel : CurrencyViewModel
    private lateinit var binding: ActivityMainBinding
    private val currencyAdapter = CurrencyAdapter(arrayListOf(),this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.user = intent.getSerializableExtra(USER_EXTRA) as User

        viewModel = ViewModelProvider(
            this, CurrencyViewModel.CurrencyViewModelFactory(CurrencyRepository()
            )
        ).get(CurrencyViewModel::class.java)

        binding.rvList.apply{
            layoutManager = GridLayoutManager(applicationContext, 2)
            adapter = currencyAdapter
        }

        viewModel.updateCurrencies()

        viewModel.currencyList.observe(this) {
            currencyAdapter.update(it)
        }

        binding.buttonRefreshCurrencies.setOnClickListener { button ->
            viewModel.updateCurrencies()
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
        }


        var resultLaucher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data: Intent? = result.data
            }
        }


        binding.buttonAddCountries.setOnClickListener {
            val intent = Intent(this, ListCountriesActivity::class.java)
            resultLaucher.launch(intent)
        }
    }
}