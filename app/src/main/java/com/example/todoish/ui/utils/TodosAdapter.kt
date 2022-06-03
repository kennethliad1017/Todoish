package com.example.todoish.ui.utils

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoish.R
import com.example.todoish.data.model.Todo
import com.example.todoish.databinding.TodoCardFragmentBinding
import com.example.todoish.ui.viewmodel.MainViewModel
import com.google.android.material.color.MaterialColors

class TodosAdapter(private val viewModel: MainViewModel) : ListAdapter<Todo, TodosAdapter.TodoViewHolder>(TodoDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(viewModel, current)
    }

    class TodoViewHolder(private val binding: TodoCardFragmentBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: MainViewModel, item: Todo) {
            binding.viewmodel = viewModel
            binding.task = item
            binding.iconDescription.setColorFilter(when(viewModel.isBefore(item.dateDue)) {
                true -> MaterialColors.getColor(itemView, com.google.android.material.R.attr.colorOnSurface)
                else -> Color.parseColor("#FF000000")
            })

            binding.completeBtn.setOnClickListener {
                viewModel.updateTodo(item.uid)
            }

            binding.editBtn.setOnClickListener { view ->
                view.findNavController().navigate(R.id.action_mainFragment_to_todoFormFragment, bundleOf("todoId" to item.uid))
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TodoViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TodoCardFragmentBinding.inflate(layoutInflater, parent, false)

                return TodoViewHolder(binding)
            }
        }
    }

    class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }
    }
}