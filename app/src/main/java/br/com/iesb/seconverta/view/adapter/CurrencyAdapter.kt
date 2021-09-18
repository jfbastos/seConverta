package br.com.iesb.seconverta.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.iesb.seconverta.R
import br.com.iesb.seconverta.databinding.CurrencyContentBinding
import br.com.iesb.seconverta.model.CurrencyItem
import br.com.iesb.seconverta.utils.Formaters
import com.google.android.material.textfield.TextInputEditText

class CurrencyAdapter(private val items: ArrayList<CurrencyItem>, private val activity : Activity) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    class CurrencyViewHolder(itemView: View, activity: Activity) : RecyclerView.ViewHolder(itemView) {
        val itemBinding = CurrencyContentBinding.bind(itemView)
        val currencyValue = activity.findViewById<TextInputEditText>(R.id.currencyValue)
        fun bind(currency: CurrencyItem, activity: Activity) {
            itemBinding.currencyName.text = currency.code
            itemBinding.currencyValue.text = Formaters.formatMoneyToString(currency.value)
            itemBinding.totalCurrencyValue.text = Formaters.formatMoneyToString((currency.value * Formaters.formatStringToDouble(currencyValue.text.toString(),activity)))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_content, parent, false)
        return CurrencyViewHolder(view,activity)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(items[position],activity)
    }

    override fun getItemCount() = items.size

    fun update(selectedCurrencies: List<CurrencyItem>) {
        items.clear()
        items.addAll(selectedCurrencies)
        notifyItemRangeChanged(0, selectedCurrencies.size)
    }
}