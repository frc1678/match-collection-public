package com.example.match_collection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.spinner_standard_cell.view.*

class StandardSpinnerAdapter(val context: Context, val spinner_item_list: ArrayList<String>) : BaseAdapter() {

    var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(R.layout.spinner_standard_cell, parent, false)
        view.spinner_item_text_display.text = spinner_item_list[position]
        return view
    }

    override fun getItem(position: Int): Any? {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return spinner_item_list.size
    }
}