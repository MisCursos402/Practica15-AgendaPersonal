package com.miscursos.practica15git

import com.miscursos.practica15git.data.model.EventItem
import com.miscursos.practica15git.data.model.NoteItem
import com.miscursos.practica15git.data.model.Priority
import com.miscursos.practica15git.data.model.ReminderItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ModelAndPriorityUnitTest {

    @Test
    fun priority_fromString_matchesCorrectly() {
        assertEquals(Priority.ALTA, Priority.fromString("ALTA"))
        assertEquals(Priority.ALTA, Priority.fromString("Alta"))
        assertEquals(Priority.MEDIA, Priority.fromString("Media"))
        assertEquals(Priority.BAJA, Priority.fromString("Baja"))
        assertEquals(Priority.MEDIA, Priority.fromString("NonExistent"))
    }

    @Test
    fun eventItem_creation_hasExpectedDefaults() {
        val event = EventItem(
            id = 1L,
            title = "Reunión",
            date = "2026-11-24",
            time = "10:00"
        )
        assertEquals("Reunión", event.title)
        assertEquals("Personal", event.category)
        assertEquals(Priority.MEDIA, event.priority)
        assertEquals("", event.location)
    }

    @Test
    fun noteItem_pinning_works() {
        val note = NoteItem(
            id = 1L,
            title = "Nota importante",
            content = "Contenido",
            isPinned = true
        )
        assertTrue(note.isPinned)
        val unpinned = note.copy(isPinned = false)
        assertFalse(unpinned.isPinned)
    }

    @Test
    fun reminderItem_completionToggle() {
        val reminder = ReminderItem(
            id = 1L,
            title = "Comprar insumos",
            date = "2026-11-24",
            time = "18:00",
            isCompleted = false,
            hasAlarm = true
        )
        assertFalse(reminder.isCompleted)
        assertTrue(reminder.hasAlarm)

        val completed = reminder.copy(isCompleted = true)
        assertTrue(completed.isCompleted)
    }
}
