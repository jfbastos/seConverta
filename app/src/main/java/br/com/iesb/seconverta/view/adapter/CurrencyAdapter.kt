package br.com.iesb.seconverta.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.iesb.seconverta.R
import br.com.iesb.seconverta.databinding.CurrencyContentBinding
import br.com.iesb.seconverta.model.CountryCode
import br.com.iesb.seconverta.model.CurrencyItem
import br.com.iesb.seconverta.utils.Formaters

class CurrencyAdapter(private val items: ArrayList<CurrencyItem>) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemBinding = CurrencyContentBinding.bind(itemView)
        //val mainViewBinding
        fun bind(currency: CurrencyItem) {
            itemBinding.currencyName.text = currency.code
            itemBinding.currencyValue.text = Formaters.formatMoneyToString(currency.value)
            itemBinding.totalCurrencyValue.text = "R$ 100 - TODO"
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

    fun update(selectedCurrencies: List<CurrencyItem>) {
        items.clear()
        items.addAll(selectedCurrencies)
        notifyDataSetChanged()
    }


}