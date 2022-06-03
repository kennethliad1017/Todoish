package com.example.todoish.domain

import com.example.todoish.data.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend operator fun  invoke(id: Int, completed: Boolean) {
        withContext(ioDispatcher) {
            todoRepository.updateTodo(id = id, completed = completed)
        }
    }
}