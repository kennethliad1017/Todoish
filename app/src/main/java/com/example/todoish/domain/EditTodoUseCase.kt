package com.example.todoish.domain

import com.example.todoish.data.model.Todo
import com.example.todoish.data.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class EditTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend operator fun invoke(
        id: Int,
        title: String,
        description: String,
        category: String,
        color: String,
        completed: Boolean,
        dateDue: Date
    ) {
        withContext(ioDispatcher) {
            todoRepository.editTodo(
                Todo(
                    uid = id,
                    title = title,
                    description = description,
                    category = category,
                    color = color,
                    isCompleted = completed,
                    dateDue = dateDue
                )
            )
        }
    }
}