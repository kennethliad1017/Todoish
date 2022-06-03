package com.example.todoish.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.todoish.data.Result
import com.example.todoish.data.model.Todo
import com.example.todoish.data.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetLatestTodoUseCase @Inject constructor(
    private val todoRepositoryImpl: TodoRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(date: Date): Flow<List<Todo>> {
        return withContext(ioDispatcher) {
            todoRepositoryImpl.fetchTodoByDate(date)
        }
    }
}