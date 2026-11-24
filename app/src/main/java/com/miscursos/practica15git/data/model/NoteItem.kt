package com.miscursos.practica15git.data.model

data class NoteItem(
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: String = "General",
    val colorHex: Long = 0xFFFFF9C4, // Default light yellow
    val isPinned: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
