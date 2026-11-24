package com.miscursos.practica15git.data.model

data class EventItem(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val date: String,
    val time: String,
    val location: String = "",
    val category: String = "Personal",
    val priority: Priority = Priority.MEDIA,
    val createdAt: Long = System.currentTimeMillis()
)
