package com.example.todoish.ui.components.tabs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import com.example.todoish.R
import com.example.todoish.databinding.FragmentTaskDoneBinding
import com.example.todoish.databinding.FragmentTodayBinding
import com.example.todoish.databinding.MainFragmentBinding
import com.example.todoish.ui.utils.TodosAdapter
import com.example.todoish.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskDoneFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var viewDataBinding: FragmentTaskDoneBinding

    private lateinit var todoAdapter: TodosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentTaskDoneBinding.inflate(inflater, container, false).apply {
            this.viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchCompletedTodo()

            viewModel.uiState
                .map { it.isFetchingTodo }
                .distinctUntilChanged()
                .observe(viewLifecycleOwner) { viewDataBinding.loading.isVisible = it }

            viewModel.uiState
                .map { it.todoItems }
                .distinctUntilChanged()
                .observe(viewLifecycleOwner) {
                    if (it.isEmpty()) {
                        viewDataBinding.emptyLabel.isVisible = true
                        viewDataBinding.todoList.isVisible = false
                    } else {
                        viewDataBinding.emptyLabel.isVisible = false
                        viewDataBinding.todoList.isVisible = true

                    }
                }
        }

        setupListAdapter()
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            todoAdapter = TodosAdapter(viewModel)
            viewDataBinding.todoList.adapter = todoAdapter
        } else {
            Log.w("androiddebug", "ViewModel not initialized when attempting to set up adapter.")
        }
    }
}