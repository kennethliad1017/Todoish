package com.example.todoish.data.model

data class TodoUiState(
    val todoItems: List<Todo> = listOf(),
    val userMessages: List<Message> = listOf(),
    val isFetchingTodo: Boolean = false,
)
