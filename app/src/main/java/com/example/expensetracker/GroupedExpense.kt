package com.example.expensetracker

data class GroupedExpense(
    val category: String,
    val expenses: List<Expense>,
    val total: Double
)
