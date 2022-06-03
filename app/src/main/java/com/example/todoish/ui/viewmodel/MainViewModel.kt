package com.example.todoish.ui.viewmodel

import android.graphics.Color
import android.text.format.DateUtils
import androidx.lifecycle.*
import com.example.todoish.data.Result
import com.example.todoish.data.model.Todo
import com.example.todoish.data.model.TodoUiState
import com.example.todoish.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val latestTodoUseCase: GetLatestTodoUseCase,
    private val getCompleteTodoUseCase: GetCompleteTodoUseCase,
    private val getFailedTodoUseCase: GetFailedTodoUseCase,
    private val getUpcomingTodoUseCase: GetUpcomingTodoUseCase,
    private val formatDateUseCase: FormatDateUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase
)  : ViewModel() {
    private val _uiState = MutableLiveData(TodoUiState())
    val uiState: LiveData<TodoUiState> = _uiState

    private val _today: MutableStateFlow<Date> by lazy {
        MutableStateFlow(Date())
    }

    val today: StateFlow<Date> get() = _today

    fun formatDate(date: Date, format: String): String {
        return formatDateUseCase(date, format)
    }

    // check if date is before the current date & time
    fun isBefore(date: Date): Boolean {
        return date.before(today.value)
    }

    // check if date day is after current day
    fun isNotToday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = today.value
        calendar.set(Calendar.HOUR, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        return !DateUtils.isToday(date.time)
    }

    // return todos set color to display as card background color
    fun color(color: String): Int {
        return Color.parseColor(color)
    }

    // fetch today's todo-list
    fun fetchTodosByDate(date: Date) {
        _uiState.value = _uiState.value?.copy(isFetchingTodo = true)
        viewModelScope.launch {
            latestTodoUseCase(date).collect {
                _uiState.postValue(_uiState.value?.copy(todoItems = it, isFetchingTodo = false))
            }
        }
    }

    // fetch the completed todo
    fun fetchCompletedTodo() {
        _uiState.value = _uiState.value?.copy(isFetchingTodo = true)
        viewModelScope.launch {
            getCompleteTodoUseCase().collect {
                _uiState.postValue(_uiState.value?.copy(todoItems = it, isFetchingTodo = false))
            }
        }
    }

    // fetch the upcoming todo
    fun fetchUpcomingTodo(date: Date) {
        _uiState.value = _uiState.value?.copy(isFetchingTodo = true)
        viewModelScope.launch {
            getUpcomingTodoUseCase(date).collect {
                _uiState.postValue(_uiState.value?.copy(todoItems = it, isFetchingTodo = false))
            }
        }
    }

    // fetch the incomplete todo / unable to update to complete task
    fun fetchFailedTodo(date: Date) {
        _uiState.value = _uiState.value?.copy(isFetchingTodo = true)
        viewModelScope.launch {
            getFailedTodoUseCase(date).collect {
                _uiState.postValue(_uiState.value?.copy(todoItems = it, isFetchingTodo = false))
            }
        }
    }

    fun updateTodo(id: Int) {
        viewModelScope.launch {
            updateTodoUseCase(id = id, completed = true)
        }
    }
}