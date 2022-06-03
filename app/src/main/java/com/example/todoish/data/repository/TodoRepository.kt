package com.example.todoish.data.repository

import androidx.lifecycle.LiveData
import com.example.todoish.data.Result
import com.example.todoish.data.model.Todo
import kotlinx.coroutines.flow.Flow
import java.util.*

interface TodoRepository {

    suspend fun createTodo(todo: Todo)

    fun fetchTodoByDate(date: Date): Flow<List<Todo>>

    fun fetchUpcomingTodo(date: Date): Flow<List<Todo>>

    fun fetchCompletedTodo(completed: Boolean): Flow<List<Todo>>

    suspend fun getTask(id: Int): Result<Todo>

    fun fetchFailedTodo(date: Date): Flow<List<Todo>>

    suspend fun updateTodo(id: Int, completed: Boolean)

    suspend fun  editTodo(todo: Todo)

    suspend fun deleteTodoById(id: Int)
}