package com.example.todoish.ui.utils


import android.widget.CalendarView
import android.widget.RadioButton
import androidx.databinding.*
import androidx.recyclerview.widget.RecyclerView
import com.example.todoish.R
import com.example.todoish.data.model.Todo
import com.example.todoish.data.model.TodoUiState
import com.google.android.material.color.MaterialColors
import io.github.mrherintsoahasina.flextools.FlexRadioGroup
import java.util.*

/**
 *  this is the location of all databinding from xml layout
 *
 *  NOTE: color radio group and category radio group didn't have same app:flexCheckedButton / app:checkedButton
 *  because either one works and the other won't work
 */

@BindingAdapter("app:item")
fun setItem(listView: RecyclerView, item: TodoUiState?) {
    (listView.adapter as TodosAdapter).submitList(item?.todoItems)
}

@BindingAdapter("app:checkedButton")
fun setColorChecked(colorRadioGroup: FlexRadioGroup, item: Int) {
    if (colorRadioGroup.checkedRadioButtonId != item) {
        colorRadioGroup.check(item)
    }
    colorRadioGroup.check(item)
}

@BindingAdapter(value = ["app:onColorCheckedChanged", "android:checkedButtonAttrChanged"], requireAll = false)
fun setListeners(view: FlexRadioGroup, listener: FlexRadioGroup.OnCheckedChangeListener?, attrChange: InverseBindingListener?) {
    if (attrChange == null) {
        view.setOnCheckedChangeListener(listener)
    } else {
        view.setOnCheckedChangeListener( FlexRadioGroup.OnCheckedChangeListener { group, checkedId ->
            listener?.onCheckedChanged(group, checkedId)

            attrChange.onChange()
        })
    }
}

@BindingAdapter("app:flexCheckedButton")
fun setCategoryChecked(categoryRadioGroup: FlexRadioGroup, item: Int) {
    if (categoryRadioGroup.checkedRadioButtonId != item) {
        categoryRadioGroup.check(item)
    }

    categoryRadioGroup.check(item)
}

@BindingAdapter(value = ["app:onCategoryCheckedChanged", "android:checkedButtonAttrChanged"], requireAll = false)
fun setCategoryListeners(view: FlexRadioGroup, categoryListener: FlexRadioGroup.OnCheckedChangeListener?, attrChange: InverseBindingListener?) {
    if (attrChange == null) {
        view.setOnCheckedChangeListener(categoryListener)
    } else {
        view.setOnCheckedChangeListener { group, checkedId ->
            categoryListener?.onCheckedChanged(group, checkedId)
            attrChange.onChange()
        }
    }
}

// TimePicker

// CalendarView
@BindingAdapter(value = ["app:onSelectedDateChange", "android:dateAttrChanged"], requireAll = false)
fun setDateListener(view: CalendarView, dateListener: CalendarView.OnDateChangeListener?, attrChange: InverseBindingListener?) {
    if (attrChange == null) {
        view.setOnDateChangeListener(dateListener)
    } else {
        view.setOnDateChangeListener { view, year, month, dayOfMonth ->
            dateListener?.onSelectedDayChange(view, year, month, dayOfMonth)
            attrChange.onChange()
        }
    }
}