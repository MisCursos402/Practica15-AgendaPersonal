package com.miscursos.practica15git.data.model

import androidx.compose.ui.graphics.Color

enum class Priority(
    val label: String,
    val color: Color
) {
    ALTA("Alta", Color(0xFFE53935)),
    MEDIA("Media", Color(0xFFFB8C00)),
    BAJA("Baja", Color(0xFF43A047));

    companion object {
        fun fromString(value: String): Priority {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.label.equals(value, ignoreCase = true) }
                ?: MEDIA
        }
    }
}
