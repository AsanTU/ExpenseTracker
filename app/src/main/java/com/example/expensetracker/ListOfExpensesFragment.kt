package com.example.expensetracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.databinding.FragmentListOfExpensesBinding
import kotlin.math.exp

@Suppress("DEPRECATION")
class ListOfExpensesFragment : Fragment() {

    private var _binding: FragmentListOfExpensesBinding? = null
    private val binding get() = _binding!!
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var preferencesHelper: PreferencesHelper
    private val expenses = mutableListOf<Expense>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOfExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigationListener()
        preferencesHelper = PreferencesHelper(requireContext())
        val savedExpenses = preferencesHelper.getExpenses()
        expenses.addAll(savedExpenses)
        expenseAdapter = ExpenseAdapter(expenses)
        binding.apply {
            expenseRv.adapter = expenseAdapter
            expenseRv.layoutManager = LinearLayoutManager(context)
            addExpenseBtn.setOnClickListener {
                findNavController().navigate(R.id.action_listOfExpensesFragment_to_addEditExpenseFragment) }
        }

        parentFragmentManager.setFragmentResultListener("newExpense", viewLifecycleOwner){_, bundle->
            val newExpense = bundle.getParcelable<Expense>("newExpense")
            if (newExpense != null){
                expenses.add(0, newExpense)
                expenseAdapter.notifyItemInserted(0)
                binding.expenseRv.scrollToPosition(0)
                preferencesHelper.saveExpenses(expenses)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val updateExpenses = preferencesHelper.getExpenses()
        expenses.clear()
        expenses.addAll(updateExpenses)
        expenseAdapter.notifyDataSetChanged()
    }

    private fun setupBottomNavigationListener() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_expenses -> {
                    val navOptions = NavOptions.Builder()
                        .setPopEnterAnim(R.anim.slide_in_right)
                        .setPopExitAnim(R.anim.slide_out_left)
                        .build()
                    findNavController().navigate(R.id.syncFragment, null, navOptions)
                    true
                }

                R.id.nav_conversion -> {
                    val navOptions = NavOptions.Builder()
                        .setPopEnterAnim(R.anim.slide_in_right)
                        .setPopExitAnim(R.anim.slide_out_left)
                        .build()
                    findNavController().navigate(R.id.currencyConversionFragment, null, navOptions)
                    true
                }

                R.id.nav_settings -> {
                    val navOptions = NavOptions.Builder()
                        .setPopEnterAnim(R.anim.slide_in_left)
                        .setPopExitAnim(R.anim.slide_out_right)
                        .build()
                    findNavController().navigate(R.id.settingsFragment, null, navOptions)
                    true
                }

                else -> false
            }
        }
    }
}