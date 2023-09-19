package com.example.todoish.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_table WHERE isCompleted = :isCompleted AND dateDue BETWEEN :date AND :maxDate  ORDER BY dateDue ASC")
    fun fetchTodoByDate(date: Date, maxDate: Date, isCompleted: Boolean): Flow<List<Todo>>

    @Query("SELECT * FROM todo_table WHERE isCompleted = :completed ORDER BY dateDue ASC")
    fun fetchCompleteTodo(completed: Boolean): Flow<List<Todo>>

    @Query("SELECT * FROM todo_table WHERE dateDue < :date AND isCompleted = :completed ORDER BY dateDue ASC")
    fun fetchFailedTodo(date: Date, completed: Boolean): Flow<List<Todo>>

    @Query("SELECT * FROM todo_table WHERE dateDue > :date ORDER BY dateDue ASC")
    fun fetchUpcomingTodo(date: Date): Flow<List<Todo>>

    @Query("SELECT * FROM todo_table WHERE uid = :id")
    fun getTodoById(id: Int): Todo?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createTodo(todo: Todo): Unit

    @Query("UPDATE todo_table SET isCompleted = :completed WHERE  uid = :id")
    suspend fun updateTodo(completed: Boolean, id: Int): Unit

    @Update
    suspend fun editTodo(todo: Todo): Unit

    @Query("DELETE FROM todo_table WHERE uid = :id")
    suspend fun removeTodoById(id: Int): Unit
}