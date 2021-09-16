package br.com.iesb.seconverta.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import br.com.iesb.seconverta.R
import br.com.iesb.seconverta.model.CurrencyRepository
import br.com.iesb.seconverta.viewModel.CurrencyViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel : CurrencyViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(
            this, CurrencyViewModel.CurrencyViewModelFactory(CurrencyRepository()
            )
        ).get(CurrencyViewModel::class.java)

        //viewModel.getCountries("latest")

//        viewModel.countriesListLiveData.observe(this){ list ->
//            Log.d("MutableList", "$list\n")
//        }

        viewModel.getCurrency("latest", "usd")


        viewModel.currencyLiveData.observe(this){
            Log.d("Currency", "${it.brl}")
        }

        viewModel.currencyList.observe(this){
            Log.d("Currency", "${it}")
        }
    }
}