package com.example.expensetracker

data class Expense(
    val amount: Double,
    val description: String,
    val category: String,
    val paymentMethod: String
)
