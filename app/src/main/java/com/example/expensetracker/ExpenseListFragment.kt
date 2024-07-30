package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.databinding.FragmentExpenseListBinding

class ExpenseListFragment : Fragment() {

    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpenseViewModel by activityViewModels()
    private lateinit var adapter: ExpenseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ExpenseAdapter()
        binding.recyclerViewExpenses.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewExpenses.adapter = adapter

        viewModel.groupedExpenses.observe(viewLifecycleOwner) { groupedExpenses ->
            adapter.submitList(groupedExpenses)
        }

        viewModel.totalExpenses.observe(viewLifecycleOwner) {
            binding.textViewTotalExpenses.text = "Overall Total: $%.2f".format(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
