package com.miscursos.practica15git.data.model

data class ReminderItem(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val date: String,
    val time: String,
    val priority: Priority = Priority.MEDIA,
    val isCompleted: Boolean = false,
    val hasAlarm: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
