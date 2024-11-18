package com.example.expensetracker

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.expensetracker.databinding.FragmentAddEditExpenseBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

@Suppress("DEPRECATION")
class AddEditExpenseFragment : Fragment() {

    private var _binding: FragmentAddEditExpenseBinding? = null
    private val binding get() = _binding!!

    private val categories = mutableListOf("Food", "Transport", "Entertainment", "Other")
    private val currencies = listOf("USD", "EUR", "KGS", "RUB")

    private var selectedDate: String? = null
    private var isEditing: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategorySpinner()
        setupCurrencySpinner()
        setupDatePicker()
        checkData()
    }

    private fun setupCategorySpinner() {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter
    }

    private fun setupCurrencySpinner() {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.currencySpinner.adapter = adapter
    }

    private fun setupDatePicker() {
        binding.selectDateBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, month, dayOfMonth)
                    selectedDate = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(selectedCalendar.time)
                    binding.dateTv.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun populateFields(expense: Expense) {
        binding.apply {
            amountEt.setText(expense.amount.toString())
            categorySpinner.setSelection(categories.indexOf(expense.category))
            currencySpinner.setSelection(categories.indexOf(expense.currency))
            selectedDate = expense.date
            dateTv.text = selectedDate
        }
    }

    private fun saveExpense() {
        val amountText = binding.amountEt.text.toString()
        val amount = amountText.toDoubleOrNull()
        if (amount == null) {
            Toast.makeText(requireContext(), "Enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val category = binding.categorySpinner.selectedItem.toString()
        val currency = binding.currencySpinner.selectedItem.toString()
        val date = selectedDate
        if (date.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Select a date", Toast.LENGTH_SHORT).show()
            return
        }
        val expense = Expense(
            id = UUID.randomUUID().toString(),
            title = category,
            amount = amount,
            category = category,
            date = date,
            currency = currency
        )

        val resultBundle = Bundle().apply {
            putParcelable("newExpense", expense)
        }
        parentFragmentManager.setFragmentResult("newExpense", resultBundle)
        if (isEditing) {
            Toast.makeText(requireContext(), "Expense updated!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Expense added!", Toast.LENGTH_SHORT).show()
        }
        parentFragmentManager.popBackStack()
    }

    private fun addNewCategory() {
        Toast.makeText(requireContext(), "Add new category clicked", Toast.LENGTH_SHORT).show()
    }

    private fun checkData() {
        val expense = arguments?.getParcelable<Expense>("expense")
        if (expense != null) {
            isEditing = true
            populateFields(expense)
        }
        binding.saveExpenseBtn.setOnClickListener { saveExpense() }
        binding.addCategoryBtn.setOnClickListener { addNewCategory() }
    }
}