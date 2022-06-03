package com.example.todoish.domain

import com.example.todoish.data.Result
import com.example.todoish.data.model.Todo
import com.example.todoish.data.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTodoByIdUseCase  @Inject constructor(
    private val todoRepository: TodoRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend operator fun invoke(id: Int): Result<Todo> {
        return withContext(ioDispatcher) {
            todoRepository.getTask(id)
        }
    }

}