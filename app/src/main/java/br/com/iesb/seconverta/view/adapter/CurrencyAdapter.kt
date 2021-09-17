package br.com.iesb.seconverta.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.iesb.seconverta.R
import br.com.iesb.seconverta.model.CountryCode
import br.com.iesb.seconverta.model.network.CurrencyApi

class CurrencyAdapter(private val items: ArrayList<CountryCode>):
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>(){

    class CurrencyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val currencyNameTextView: TextView = itemView.findViewById(R.id.currencyName)
        private val currencyNameDetailTextView: TextView = itemView.findViewById(R.id.currencyNameDetail)
        var currentCurrency: CurrencyApi? = null

        fun bind(currency: CountryCode) {

            currencyNameTextView.text = currency.toString()
            currencyNameDetailTextView.text = currency.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_content, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}