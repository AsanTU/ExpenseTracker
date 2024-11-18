package com.example.expensetracker

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.ItemExpenseBinding

class ExpenseAdapter(private val expenses: List<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    inner class ExpenseViewHolder(private val binding: ItemExpenseBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(expense: Expense){
            binding.expenseTitleTv.text = expense.category
            binding.expenseDateTv.text = expense.date
            binding.expenseAmountTv.text = "${expense.amount} ${expense.currency}"

            when (expense.category) {
                "Food" -> binding.categoryIconIv.setImageResource(R.drawable.ic_food)
                "Transport" -> binding.categoryIconIv.setImageResource(R.drawable.ic_transport)
                "Entertainment" -> binding.categoryIconIv.setImageResource(R.drawable.ic_entertainment)
                else -> binding.categoryIconIv.setImageResource(R.drawable.ic_category)
            }

            binding.expenseAmountTv.setTextColor(
                if (expense.amount >= 0) binding.root.context.getColor(R.color.bright_green)
                else binding.root.context.getColor(R.color.bright_red)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        Log.d("ExpenseAdapter", "Desplay: $expense")
        holder.bind(expense)
    }

    override fun getItemCount(): Int {
        return expenses.size
    }
}