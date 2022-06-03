package com.example.todoish.domain

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FormatDateUseCase @Inject constructor() {

    operator fun invoke(date: Date, pattern: String): String {
        return SimpleDateFormat(pattern).format(date)
    }
}