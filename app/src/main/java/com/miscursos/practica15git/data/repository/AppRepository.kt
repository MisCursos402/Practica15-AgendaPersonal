package com.miscursos.practica15git.data.repository

import android.content.Context
import com.miscursos.practica15git.data.db.DatabaseHelper
import com.miscursos.practica15git.data.model.EventItem
import com.miscursos.practica15git.data.model.NoteItem
import com.miscursos.practica15git.data.model.ReminderItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context.applicationContext)
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    private val _events = MutableStateFlow<List<EventItem>>(emptyList())
    val events: StateFlow<List<EventItem>> = _events.asStateFlow()

    private val _notes = MutableStateFlow<List<NoteItem>>(emptyList())
    val notes: StateFlow<List<NoteItem>> = _notes.asStateFlow()

    private val _reminders = MutableStateFlow<List<ReminderItem>>(emptyList())
    val reminders: StateFlow<List<ReminderItem>> = _reminders.asStateFlow()

    init {
        refreshAll()
    }

    fun refreshAll() {
        repositoryScope.launch {
            loadEvents()
            loadNotes()
            loadReminders()
        }
    }

    private suspend fun loadEvents() = withContext(Dispatchers.IO) {
        _events.value = dbHelper.getAllEvents()
    }

    private suspend fun loadNotes() = withContext(Dispatchers.IO) {
        _notes.value = dbHelper.getAllNotes()
    }

    private suspend fun loadReminders() = withContext(Dispatchers.IO) {
        _reminders.value = dbHelper.getAllReminders()
    }

    // ================= EVENTS =================
    suspend fun addEvent(event: EventItem): Long = withContext(Dispatchers.IO) {
        val id = dbHelper.insertEvent(event)
        loadEvents()
        id
    }

    suspend fun updateEvent(event: EventItem) = withContext(Dispatchers.IO) {
        dbHelper.updateEvent(event)
        loadEvents()
    }

    suspend fun deleteEvent(id: Long) = withContext(Dispatchers.IO) {
        dbHelper.deleteEvent(id)
        loadEvents()
    }

    // ================= NOTES =================
    suspend fun addNote(note: NoteItem): Long = withContext(Dispatchers.IO) {
        val id = dbHelper.insertNote(note)
        loadNotes()
        id
    }

    suspend fun updateNote(note: NoteItem) = withContext(Dispatchers.IO) {
        dbHelper.updateNote(note)
        loadNotes()
    }

    suspend fun deleteNote(id: Long) = withContext(Dispatchers.IO) {
        dbHelper.deleteNote(id)
        loadNotes()
    }

    suspend fun toggleNotePin(id: Long, isPinned: Boolean) = withContext(Dispatchers.IO) {
        dbHelper.toggleNotePin(id, isPinned)
        loadNotes()
    }

    // ================= REMINDERS =================
    suspend fun addReminder(reminder: ReminderItem): Long = withContext(Dispatchers.IO) {
        val id = dbHelper.insertReminder(reminder)
        loadReminders()
        id
    }

    suspend fun updateReminder(reminder: ReminderItem) = withContext(Dispatchers.IO) {
        dbHelper.updateReminder(reminder)
        loadReminders()
    }

    suspend fun deleteReminder(id: Long) = withContext(Dispatchers.IO) {
        dbHelper.deleteReminder(id)
        loadReminders()
    }

    suspend fun toggleReminderCompletion(id: Long, isCompleted: Boolean) = withContext(Dispatchers.IO) {
        dbHelper.toggleReminderCompletion(id, isCompleted)
        loadReminders()
    }
}
