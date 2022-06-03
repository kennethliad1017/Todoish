package com.example.todoish.data.model

import androidx.room.*
import java.util.*

@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "color") val color: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "isCompleted") val isCompleted: Boolean = false,
    @ColumnInfo(name = "dateDue") val dateDue: Date,
)
