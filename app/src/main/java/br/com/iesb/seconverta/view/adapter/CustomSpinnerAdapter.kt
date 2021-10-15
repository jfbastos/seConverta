package br.com.iesb.seconverta.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.iesb.seconverta.R
import java.lang.Exception


class CustomArrayAdapter(getContext: Context, private val listOfCodes: List<String>) : BaseAdapter() {

    private var inflater : LayoutInflater = LayoutInflater.from(getContext)

    override fun getCount(): Int {
        return listOfCodes.size
    }

    override fun getItem(position: Int): Any {
        return listOfCodes[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(R.layout.spinner_item,null)
        return try{
            val currencyCode = view.findViewById<TextView>(R.id.spinnerTarget)
            currencyCode.text = listOfCodes[position]
            view
        }catch (e : Exception){
            view
        }

    }


}