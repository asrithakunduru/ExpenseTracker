package com.example.expensetracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExpenseViewModel : ViewModel() {
    private val _expenses = MutableLiveData<MutableList<Expense>>(mutableListOf())
    val expenses: LiveData<MutableList<Expense>> get() = _expenses

    private val _groupedExpenses = MutableLiveData<List<GroupedExpense>>(emptyList())
    val groupedExpenses: LiveData<List<GroupedExpense>> get() = _groupedExpenses

    private val _totalExpenses = MutableLiveData<Double>(0.0)
    val totalExpenses: LiveData<Double> get() = _totalExpenses

    fun addExpense(expense: Expense) {
        _expenses.value?.add(expense)
        _expenses.value = _expenses.value
        updateGroupedExpenses()
    }

    private fun updateGroupedExpenses() {
        val grouped = _expenses.value?.groupBy { it.category }?.map { (category, expenses) ->
            GroupedExpense(
                category = category,
                expenses = expenses,
                total = expenses.sumOf { it.amount }
            )
        } ?: emptyList()
        _groupedExpenses.value = grouped
        calculateTotalExpenses()
    }

    private fun calculateTotalExpenses() {
        _totalExpenses.value = _expenses.value?.sumOf { it.amount } ?: 0.0
    }
}
