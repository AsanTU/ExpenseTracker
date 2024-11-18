package com.example.expensetracker

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesHelper( context: Context) {

    private val sharedPreferences = context.getSharedPreferences("expense_tracker_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveExpenses(expense: List<Expense>){
        val json = gson.toJson(expense)
        sharedPreferences.edit().putString("expenses", json).apply()
    }

    fun getExpenses(): List<Expense>{
        val json = sharedPreferences.getString("expenses", null)
        return if (json!= null){
            val type = object : TypeToken<List<Expense>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}