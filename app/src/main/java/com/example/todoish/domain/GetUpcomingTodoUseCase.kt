package com.example.todoish.domain

import com.example.todoish.data.model.Todo
import com.example.todoish.data.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class GetUpcomingTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
){
    suspend operator fun invoke(date: Date): Flow<List<Todo>> {
        return withContext(ioDispatcher) {
            todoRepository.fetchUpcomingTodo(date)
        }
    }
}