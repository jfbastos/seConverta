package br.com.iesb.seconverta.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.iesb.seconverta.R
import br.com.iesb.seconverta.databinding.CurrencyContentBinding
import br.com.iesb.seconverta.model.Currency
import br.com.iesb.seconverta.utils.Formaters
import com.google.android.material.textfield.TextInputEditText

class CurrencyAdapter(
    private val items: ArrayList<Currency>,
    private val activity: Activity,
    private val listener: OnLongItemClickListener
) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    inner class CurrencyViewHolder(itemView: View, activity: Activity) :
        RecyclerView.ViewHolder(itemView), View.OnLongClickListener {
        private val itemBinding = CurrencyContentBinding.bind(itemView)
        private val currencyValue = activity.findViewById<TextInputEditText>(R.id.currencyValue)


        init {
            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View?): Boolean {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onLongItemClick(items[adapterPosition])
            }
            return true
        }

        fun bind(currency: Currency, activity: Activity) {
            itemBinding.currencyName.text = currency.code
            itemBinding.currencyItemValue.text =
                "${Formaters.formatMoneyToString(1 / currency.value)} = R$ 1,00"
            itemBinding.totalCurrencyValue.text = "${currency.code} ${
                Formaters.formatMoneyToString(
                    (Formaters.formatStringToDouble(
                        currencyValue.text.toString(),
                        activity
                    ) / currency.value)
                )
            }"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_content, parent, false)
        return CurrencyViewHolder(view, activity)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(items[position], activity)
    }

    override fun getItemCount() = items.size

    fun update(selectedCurrencies: List<Currency>) {
        items.clear()
        items.addAll(selectedCurrencies)
        notifyItemRangeChanged(0, selectedCurrencies.size)
    }

    fun updateDeleted(currency: Currency) {
        val position = items.indexOf(currency)
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    interface OnLongItemClickListener {
        fun onLongItemClick(currency: Currency)
    }
}