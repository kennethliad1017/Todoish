package com.example.todoish.data.repository

import com.example.todoish.data.Result
import com.example.todoish.data.model.Todo
import com.example.todoish.data.model.TodoDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepositoryImpl @Inject constructor(private val todoDao: TodoDao) : TodoRepository {
    override suspend fun createTodo(todo: Todo) {
        todoDao.createTodo(todo = todo)
    }

    override fun fetchTodoByDate(date: Date): Flow<List<Todo>> {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return todoDao.fetchTodoByDate(date, calendar.time, false)
    }

    override fun fetchUpcomingTodo(date: Date): Flow<List<Todo>> {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return todoDao.fetchUpcomingTodo(calendar.time)
    }

    override fun fetchCompletedTodo(completed: Boolean): Flow<List<Todo>> {
        return todoDao.fetchCompleteTodo(completed)
    }

    override fun fetchFailedTodo(date: Date): Flow<List<Todo>> {
        return todoDao.fetchFailedTodo(date, false)
    }

    override suspend fun updateTodo(id: Int, completed: Boolean) {
        todoDao.updateTodo(id = id, completed = completed)
    }

    override suspend fun editTodo(todo: Todo) {
        todoDao.editTodo(todo)
    }

    override suspend fun getTask(id: Int): Result<Todo> {
        return try {
            val task = todoDao.getTodoById(id)
            if (task != null) {
                Result.Success(task)
            } else {
                Result.Error(Exception("Todo not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteTodoById(id: Int) {
        todoDao.removeTodoById(id = id)
    }
}