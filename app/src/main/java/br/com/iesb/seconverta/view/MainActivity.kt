package br.com.iesb.seconverta.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import br.com.iesb.seconverta.R
import br.com.iesb.seconverta.databinding.ActivityMainBinding
import br.com.iesb.seconverta.model.CurrencyRepository
import br.com.iesb.seconverta.view.adapter.CurrencyAdapter
import br.com.iesb.seconverta.viewModel.CurrencyViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel : CurrencyViewModel
    private lateinit var binding: ActivityMainBinding
    private val currencyAdapter = CurrencyAdapter(arrayListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(
            this, CurrencyViewModel.CurrencyViewModelFactory(CurrencyRepository()
            )
        ).get(CurrencyViewModel::class.java)

        binding.rvList.apply{
            layoutManager = GridLayoutManager(applicationContext, 2)
            adapter = currencyAdapter
        }

        viewModel.currencyList.observe(this) {
            currencyAdapter.update(it)
        }





    }
}