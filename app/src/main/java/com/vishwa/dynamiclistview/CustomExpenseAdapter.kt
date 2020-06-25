package com.vishwa.dynamiclistview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CustomExpenseAdapter (private val context: Context, private val expenses: MutableList<Expense>) : BaseAdapter() {
    private val inflater: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val dataItem = expenses[position]

        var view: View
        if(convertView == null) {
            view = inflater.inflate(R.layout.list_item, parent, false)
            view.tag = ViewHolder(view)
        } else {
            view = convertView
        }

        val viewHolder = view.tag as ViewHolder
        viewHolder.itemName.text = dataItem.item
        viewHolder.price.text = dataItem.getFormattedPrice()

        return view
    }

    override fun getItem(position: Int): Any = expenses[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = expenses.size

    class ViewHolder(view: View){
        val itemName: TextView = view.findViewById(R.id.item_name)
        val price: TextView = view.findViewById(R.id.price)
    }
}