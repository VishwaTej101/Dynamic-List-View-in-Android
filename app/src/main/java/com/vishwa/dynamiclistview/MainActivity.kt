package com.vishwa.dynamiclistview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.gson.Gson
import androidx.appcompat.app.AlertDialog

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var expensesList: ListView
    private lateinit var expenseAdapter: CustomExpenseAdapter
    private val expenseData = mutableListOf<Expense>()

    private val SHARED_PREFERENCE_NAME = "ExpensesData"
    private val DATA_KEY = "expenses"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchData()

        expensesList = findViewById(R.id.expenses_lists)
        expenseAdapter = CustomExpenseAdapter(this, expenseData)
        expensesList.adapter = expenseAdapter

        add.setOnClickListener {
            addItemDialog()
        }
        reset.setOnClickListener {
            resetDialog()
        }
    }

    private fun resetDialog() {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle("Reset Data")
            setMessage("Do you want to reset the app data?")

            //sets positive button
            setPositiveButton("Yes") { _, _ ->
                expenseData.clear()
                saveData()
            }

            //sets negative button
            setNegativeButton("No") { _, _ ->
            }

            //creates the dialog
            show()
        }
    }

    private fun addItemDialog() {
        val inflater = this.layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_add,null)
        val item = view.findViewById<EditText>(R.id.item_name)
        val price = view.findViewById<EditText>(R.id.price)

        setErrorListener(item)
        setErrorListener(price)

        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle("Add Item")
            setMessage("Please enter the details of expense item")
            setView(view)

            //sets positive button
            setPositiveButton("Add") { _, _ ->
                val itemName = item.text.toString()
                val itemPrice = price.text.toString().toFloatOrNull()
                if(itemName.isNotEmpty() && itemPrice!=null) {
                    expenseData.add(Expense(itemName, itemPrice!!))
                    saveData()
                }
                else
                    Toast.makeText(this@MainActivity,"Invalid Input", Toast.LENGTH_SHORT).show()
            }

            //sets negative button
            setNegativeButton("Cancel") { _, _ ->
            }

            //creates the dialog
            show()
        }
    }

    private fun setErrorListener(editText: EditText) {
        editText.error = if(editText.text.toString().isNotEmpty()) null else "Field Cannot be Empty"
        editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editText.error = if(editText.text.toString().isNotEmpty()) null else "Field Cannot be Empty"
            }
        })
    }

    private fun fetchData() {
        val sharedPref = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val cities = sharedPref.getStringSet(DATA_KEY, null)
        val gson = Gson()
        cities?.forEach{
            expenseData.add(gson.fromJson(it, Expense::class.java))
        }
        total.text = Expense.total(expenseData)
    }

    private fun saveData() {
        val gson = Gson()
        val cities = expenseData.map { gson.toJson(it) }
        val sharedPref = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putStringSet(DATA_KEY, cities.toSet())
            commit()
        }
        expenseAdapter.notifyDataSetChanged()
        total.text = Expense.total(expenseData)
    }
}
