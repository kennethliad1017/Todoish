package com.example.todoish.domain

import com.example.todoish.data.model.Todo
import com.example.todoish.data.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


class AddTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    suspend operator fun invoke(title: String, description: String, color: String, dueDate: Date, category: String) {
        withContext(ioDispatcher) {
            todoRepository.createTodo(Todo(title = title, description = description, color = color, dateDue = dueDate, category = category))
        }
    }
}