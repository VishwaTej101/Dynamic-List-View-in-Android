package com.vishwa.dynamiclistview

import java.text.NumberFormat
import java.util.*

data class Expense(val item: String,
                   val price: Float) {

    fun getFormattedPrice(): String = formatter.format(price)

    companion object {
        private val formatter: NumberFormat =  NumberFormat.getCurrencyInstance()
        init {
            formatter.currency = Currency.getInstance("INR")
        }

        fun total(expenses : MutableList<Expense>) : String {
            var total: Float = 0f
            expenses.forEach {
                total += it.price
            }
            return formatter.format(total)
        }
    }
}