package com.example.todoish.domain

import com.example.todoish.data.model.Todo
import com.example.todoish.data.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class RemoveTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke(id: Int) {
        withContext(defaultDispatcher) {
            todoRepository.deleteTodoById(id)
        }
    }
}