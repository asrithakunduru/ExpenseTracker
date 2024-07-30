// AddExpenseDialogFragment.kt
package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.example.expensetracker.databinding.DialogAddExpenseBinding

class AddExpenseDialogFragment : DialogFragment() {

    private var _binding: DialogAddExpenseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpenseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogAddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categories = arrayOf("Food", "Transportation", "Entertainment")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        binding.buttonSave.setOnClickListener {
            val amount = binding.editTextAmount.text.toString().toDoubleOrNull() ?: 0.0
            val description = binding.editTextDescription.text.toString()
            val category = binding.spinnerCategory.selectedItem.toString()
            val paymentMethod = when (binding.radioGroupPaymentMethod.checkedRadioButtonId) {
                R.id.radioButtonCash -> "Cash"
                R.id.radioButtonCard -> "Card"
                else -> ""
            }

            if (amount > 0) {
                val expense = Expense(amount, description, category, paymentMethod)
                viewModel.addExpense(expense)
                dismiss()
            } else {
                binding.editTextAmount.error = "Amount must be greater than 0"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
