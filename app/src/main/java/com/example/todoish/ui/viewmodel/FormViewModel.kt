package com.example.todoish.ui.viewmodel

import android.app.TimePickerDialog
import android.content.Context
import android.widget.CalendarView
import android.widget.RadioButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoish.R
import com.example.todoish.data.Result
import com.example.todoish.data.model.Todo
import com.example.todoish.domain.*
import com.example.todoish.ui.Event
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mrherintsoahasina.flextools.FlexRadioGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    private val addTodoUseCase: AddTodoUseCase,
    private val editTodoUseCase: EditTodoUseCase,
    private val getTodoByIdUseCase: GetTodoByIdUseCase,
    private val removeTodoUseCase: RemoveTodoUseCase,
    private val formatDateUseCase: FormatDateUseCase
) : ViewModel() {
    // Two-way databinding, exposing MutableLiveData
    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    val category = MutableStateFlow("No Category")
    val color = MutableStateFlow("#FFAAA7DF")
    val dateDue = MutableStateFlow(Date())

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _taskUpdatedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdateEvent: LiveData<Event<Unit>> = _taskUpdatedEvent

    private var taskId: Int = 0

    private var isNewTask: Boolean = false
    val newTask get() = isNewTask

    private var isDataLoaded = false

    private var taskCompleted = false

    private val calendar = Calendar.getInstance()

    val categoryId = MutableStateFlow(R.id.category1)

    val colorId = MutableStateFlow(R.id.color1)

    fun start(taskId: Int) {
        if (_dataLoading.value == true) {
            return
        }

        this.taskId = taskId
        if (taskId == 0) {
            isNewTask = true
            return
        }
        if (isDataLoaded) {
            return
        }

        isNewTask = false
        _dataLoading.value = true

        viewModelScope.launch {
            getTodoByIdUseCase(taskId).let { result ->
                if (result is Result.Success) {
                    onTodoLoaded(result.data)
                } else {
                    onDataNotAvailable()
                }
            }
        }
    }

    private fun onTodoLoaded(todo: Todo) {
        title.value = todo.title
        description.value = todo.description
        taskCompleted = todo.isCompleted
        dateDue.value = todo.dateDue
        category.value = todo.category
        color.value = todo.color

        // set the task date to display time and date separately
        calendar.time = todo.dateDue

        // color radio button id
        colorId.value = when (todo.color) {
            "#FF007FFF" -> R.id.color2

            "#FF31C09C" -> R.id.color3

            "#FFFF6B7F" -> R.id.color4

            "#FFFFB900" -> R.id.color5

            "#FF9740DD" -> R.id.color6

            "#FFFF91AD" -> R.id.color7

            "#FFFA9775" -> R.id.color8

            else -> R.id.color1
        }

        // category radio button id
        categoryId.value = when (category.value) {
            "Work" -> R.id.category2
            "Personal" -> R.id.category3
            "Wishlist" -> R.id.category4
            "Birthday" -> R.id.category5
            else -> R.id.category1
        }

        _dataLoading.value = false
        isDataLoaded = true
    }

    private fun onDataNotAvailable() {
        _dataLoading.value = false
    }

    fun dateToString(date: Date): String {
        return formatDateUseCase(date, "hh:mm a")
    }

    fun isCategoryEqualTo(it: Int): Boolean {
        return categoryId.value == it
    }

    fun onColorCheckedChanged(group: FlexRadioGroup, checkedId: Int) {
        this.color.value = when (checkedId) {
            R.id.color2 -> "#FF007FFF"

            R.id.color3 -> "#FF31C09C"

            R.id.color4 -> "#FFFF6B7F" //FFFA5A6F

            R.id.color5 -> "#FFFFB900"

            R.id.color6 -> "#FF9740DD"

            R.id.color7 -> "#FFFF91AD"

            R.id.color8 -> "#FFFA9775"

            else -> "#FFAAA7DF"
        }

        colorId.value = checkedId
    }

    fun onCategoryCheckedChanged(group: FlexRadioGroup, checkedId: Int) {

        if (categoryId.value != checkedId) {
            group.findViewById<RadioButton>(categoryId.value).setTextColor(
                MaterialColors.getColor(group, com.google.android.material.R.attr.colorOnBackground)
            )
            group.findViewById<RadioButton>(checkedId).setTextColor(
                MaterialColors.getColor(group, com.google.android.material.R.attr.colorOnSecondary)
            )
        }

        this.category.value = when (checkedId) {
            R.id.category2 -> "Work"

            R.id.category3 -> "Personal"

            R.id.category4 -> "Wishlist"

            R.id.category5 -> "Birthday"

            else -> "No Category"
        }

        categoryId.value = checkedId
    }

    fun onDateChangeListener(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        dateDue.value = calendar.time
    }

    fun timePicker(context: Context) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                dateDue.value = calendar.time
                // needs to set text to time in edittext
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
        ).show()
    }


    // backend actions

    fun saveTask() {
        val currentTitle = title.value
        val currentDescription = description.value
        val currentCategory = category.value
        val currentColor = color.value
        val currentDateDue =
            calendar.time // date variable should be aware of changes of date and time

        println("data: $currentTitle || $currentDescription || $currentCategory || $currentColor || $currentDateDue")
        if (currentTitle == "" || currentDescription == "" || currentCategory == "" || currentColor == "" || currentDateDue == null) {
            return
        }

        val currentTaskId = taskId
        if (isNewTask || currentTaskId == 0) {
            createTask(
                title = currentTitle,
                description = currentDescription,
                category = currentCategory,
                color = currentColor,
                dateDue = currentDateDue
            )
        } else {
            updateTask(
                id = currentTaskId,
                title = currentTitle,
                description = currentDescription,
                category = currentCategory,
                color = currentColor,
                completed = taskCompleted,
                dateDue = currentDateDue
            )
        }
    }

    private fun createTask(
        title: String,
        description: String,
        category: String,
        color: String,
        dateDue: Date
    ) {
        viewModelScope.launch {
            addTodoUseCase(
                title = title,
                description = description,
                category = category,
                color = color,
                dueDate = dateDue
            )
            _taskUpdatedEvent.value = Event(Unit)
        }
    }

    private fun updateTask(
        id: Int,
        title: String,
        description: String,
        category: String,
        color: String,
        completed: Boolean,
        dateDue: Date
    ) {
        if (isNewTask) {
            throw RuntimeException("updateTask() was called but task is new")
        }

        viewModelScope.launch {
            editTodoUseCase(id, title, description, category, color, completed, dateDue)
            _taskUpdatedEvent.value = Event(Unit)
        }
    }

    fun deleteTask() {
        if (isNewTask ) {
            throw RuntimeException("deleteTask() was called but task is new")
        }

        if (taskId != 0) {
            viewModelScope.launch {
                removeTodoUseCase(taskId)
                _taskUpdatedEvent.value = Event(Unit)
            }
        }
    }

}