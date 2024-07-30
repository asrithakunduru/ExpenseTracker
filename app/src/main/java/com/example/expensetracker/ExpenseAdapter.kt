package com.example.expensetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.ItemExpenseBinding
import com.example.expensetracker.databinding.ItemExpenseCategoryHeaderBinding

class ExpenseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var groupedExpenses: List<GroupedExpense> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemExpenseCategoryHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            VIEW_TYPE_ITEM -> {
                val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val (categoryIndex, relativePosition) = getCategoryIndexAndPosition(position)
        val groupedExpense = groupedExpenses[categoryIndex]

        if (relativePosition == 0) {
            (holder as HeaderViewHolder).bind(groupedExpense)
        } else {
            (holder as ItemViewHolder).bind(groupedExpense.expenses[relativePosition - 1])
        }
    }

    override fun getItemCount(): Int {
        return groupedExpenses.sumOf { it.expenses.size + 1 }
    }

    override fun getItemViewType(position: Int): Int {
        val (categoryIndex, relativePosition) = getCategoryIndexAndPosition(position)
        return if (relativePosition == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    fun submitList(list: List<GroupedExpense>) {
        groupedExpenses = list
        notifyDataSetChanged()
    }

    private fun getCategoryIndexAndPosition(position: Int): Pair<Int, Int> {
        var currentPosition = 0
        groupedExpenses.forEachIndexed { index, groupedExpense ->
            if (position == currentPosition) {
                return Pair(index, 0)
            }
            currentPosition += groupedExpense.expenses.size + 1 // +1 for header
            if (position < currentPosition) {
                return Pair(index, position - (currentPosition - groupedExpense.expenses.size - 1))
            }
        }
        return Pair(-1, -1) // Invalid
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    inner class HeaderViewHolder(private val binding: ItemExpenseCategoryHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(groupedExpense: GroupedExpense) {
            binding.textViewCategory.text = "${groupedExpense.category} - Total: $%.2f".format(groupedExpense.total)
        }
    }

    inner class ItemViewHolder(private val binding: ItemExpenseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: Expense) {
            binding.textViewDescription.text = expense.description
            binding.textViewAmount.text = "$%.2f".format(expense.amount)
        }
    }
}
