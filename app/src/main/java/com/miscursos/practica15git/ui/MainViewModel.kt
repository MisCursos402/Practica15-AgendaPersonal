package com.miscursos.practica15git.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miscursos.practica15git.data.model.EventItem
import com.miscursos.practica15git.data.model.NoteItem
import com.miscursos.practica15git.data.model.ReminderItem
import com.miscursos.practica15git.data.repository.AppRepository
import com.miscursos.practica15git.receiver.AlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppTab(val title: String) {
    AGENDA("Agenda"),
    NOTAS("Notas"),
    RECORDATORIOS("Recordatorios")
}

enum class ReminderFilter(val label: String) {
    TODOS("Todos"),
    PENDIENTES("Pendientes"),
    COMPLETADOS("Completados"),
    HOY("Para Hoy")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application)

    // Navigation & Global UI state
    private val _currentTab = MutableStateFlow(AppTab.AGENDA)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Agenda state
    private val _selectedDate = MutableStateFlow(getTodayDateString())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _filterBySelectedDate = MutableStateFlow(true)
    val filterBySelectedDate: StateFlow<Boolean> = _filterBySelectedDate.asStateFlow()

    // Notes state
    private val _selectedNoteCategory = MutableStateFlow<String?>(null)
    val selectedNoteCategory: StateFlow<String?> = _selectedNoteCategory.asStateFlow()

    // Reminders state
    private val _reminderFilter = MutableStateFlow(ReminderFilter.TODOS)
    val reminderFilter: StateFlow<ReminderFilter> = _reminderFilter.asStateFlow()

    // Dialogs state
    private val _editingEvent = MutableStateFlow<EventItem?>(null)
    val editingEvent: StateFlow<EventItem?> = _editingEvent.asStateFlow()

    private val _showEventDialog = MutableStateFlow(false)
    val showEventDialog: StateFlow<Boolean> = _showEventDialog.asStateFlow()

    private val _editingNote = MutableStateFlow<NoteItem?>(null)
    val editingNote: StateFlow<NoteItem?> = _editingNote.asStateFlow()

    private val _showNoteDialog = MutableStateFlow(false)
    val showNoteDialog: StateFlow<Boolean> = _showNoteDialog.asStateFlow()

    private val _editingReminder = MutableStateFlow<ReminderItem?>(null)
    val editingReminder: StateFlow<ReminderItem?> = _editingReminder.asStateFlow()

    private val _showReminderDialog = MutableStateFlow(false)
    val showReminderDialog: StateFlow<Boolean> = _showReminderDialog.asStateFlow()

    // Filtered Events Flow
    val filteredEvents: StateFlow<List<EventItem>> = combine(
        repository.events,
        _searchQuery,
        _selectedDate,
        _filterBySelectedDate
    ) { events, query, date, filterByDate ->
        events.filter { event ->
            val matchesQuery = query.isBlank() ||
                    event.title.contains(query, ignoreCase = true) ||
                    event.description.contains(query, ignoreCase = true) ||
                    event.location.contains(query, ignoreCase = true) ||
                    event.category.contains(query, ignoreCase = true)

            val matchesDate = !filterByDate || event.date == date
            matchesQuery && matchesDate
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Notes Flow
    val filteredNotes: StateFlow<List<NoteItem>> = combine(
        repository.notes,
        _searchQuery,
        _selectedNoteCategory
    ) { notes, query, category ->
        notes.filter { note ->
            val matchesQuery = query.isBlank() ||
                    note.title.contains(query, ignoreCase = true) ||
                    note.content.contains(query, ignoreCase = true)

            val matchesCategory = category == null || note.category.equals(category, ignoreCase = true)
            matchesQuery && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Reminders Flow
    val filteredReminders: StateFlow<List<ReminderItem>> = combine(
        repository.reminders,
        _searchQuery,
        _reminderFilter
    ) { reminders, query, filter ->
        val todayStr = getTodayDateString()
        reminders.filter { reminder ->
            val matchesQuery = query.isBlank() ||
                    reminder.title.contains(query, ignoreCase = true) ||
                    reminder.description.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                ReminderFilter.TODOS -> true
                ReminderFilter.PENDIENTES -> !reminder.isCompleted
                ReminderFilter.COMPLETADOS -> reminder.isCompleted
                ReminderFilter.HOY -> reminder.date == todayStr
            }
            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All available note categories
    val noteCategories: StateFlow<List<String>> = repository.notes.combine(_selectedNoteCategory) { notes, _ ->
        val list = notes.map { it.category }.filter { it.isNotBlank() }.distinct()
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Statistics
    val stats: StateFlow<Triple<Int, Int, Int>> = combine(
        repository.events,
        repository.notes,
        repository.reminders
    ) { events, notes, reminders ->
        val todayStr = getTodayDateString()
        val todayEvents = events.count { it.date == todayStr }
        val totalNotes = notes.size
        val pendingReminders = reminders.count { !it.isCompleted }
        Triple(todayEvents, totalNotes, pendingReminders)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Triple(0, 0, 0))

    // UI Actions
    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedDate(date: String) {
        _selectedDate.value = date
        _filterBySelectedDate.value = true
    }

    fun toggleFilterByDate(enabled: Boolean) {
        _filterBySelectedDate.value = enabled
    }

    fun setSelectedNoteCategory(category: String?) {
        _selectedNoteCategory.value = category
    }

    fun setReminderFilter(filter: ReminderFilter) {
        _reminderFilter.value = filter
    }

    // Event Dialog Actions
    fun openAddEventDialog() {
        _editingEvent.value = null
        _showEventDialog.value = true
    }

    fun openEditEventDialog(event: EventItem) {
        _editingEvent.value = event
        _showEventDialog.value = true
    }

    fun closeEventDialog() {
        _showEventDialog.value = false
        _editingEvent.value = null
    }

    fun saveEvent(title: String, desc: String, date: String, time: String, location: String, category: String, priority: com.miscursos.practica15git.data.model.Priority) {
        viewModelScope.launch {
            val current = _editingEvent.value
            if (current != null) {
                repository.updateEvent(
                    current.copy(
                        title = title,
                        description = desc,
                        date = date,
                        time = time,
                        location = location,
                        category = category,
                        priority = priority
                    )
                )
            } else {
                repository.addEvent(
                    EventItem(
                        title = title,
                        description = desc,
                        date = date,
                        time = time,
                        location = location,
                        category = category,
                        priority = priority
                    )
                )
            }
            closeEventDialog()
        }
    }

    fun deleteEvent(id: Long) {
        viewModelScope.launch {
            repository.deleteEvent(id)
        }
    }

    // Note Dialog Actions
    fun openAddNoteDialog() {
        _editingNote.value = null
        _showNoteDialog.value = true
    }

    fun openEditNoteDialog(note: NoteItem) {
        _editingNote.value = note
        _showNoteDialog.value = true
    }

    fun closeNoteDialog() {
        _showNoteDialog.value = false
        _editingNote.value = null
    }

    fun saveNote(title: String, content: String, category: String, colorHex: Long) {
        viewModelScope.launch {
            val current = _editingNote.value
            if (current != null) {
                repository.updateNote(
                    current.copy(
                        title = title,
                        content = content,
                        category = category,
                        colorHex = colorHex
                    )
                )
            } else {
                repository.addNote(
                    NoteItem(
                        title = title,
                        content = content,
                        category = category,
                        colorHex = colorHex
                    )
                )
            }
            closeNoteDialog()
        }
    }

    fun toggleNotePin(note: NoteItem) {
        viewModelScope.launch {
            repository.toggleNotePin(note.id, !note.isPinned)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    // Reminder Dialog Actions
    fun openAddReminderDialog() {
        _editingReminder.value = null
        _showReminderDialog.value = true
    }

    fun openEditReminderDialog(reminder: ReminderItem) {
        _editingReminder.value = reminder
        _showReminderDialog.value = true
    }

    fun closeReminderDialog() {
        _showReminderDialog.value = false
        _editingReminder.value = null
    }

    fun saveReminder(title: String, desc: String, date: String, time: String, priority: com.miscursos.practica15git.data.model.Priority, hasAlarm: Boolean) {
        viewModelScope.launch {
            val current = _editingReminder.value
            if (current != null) {
                val updated = current.copy(
                    title = title,
                    description = desc,
                    date = date,
                    time = time,
                    priority = priority,
                    hasAlarm = hasAlarm
                )
                repository.updateReminder(updated)
                if (hasAlarm && !updated.isCompleted) {
                    AlarmScheduler.scheduleReminder(getApplication(), updated)
                } else {
                    AlarmScheduler.cancelReminder(getApplication(), updated.id)
                }
            } else {
                val newReminder = ReminderItem(
                    title = title,
                    description = desc,
                    date = date,
                    time = time,
                    priority = priority,
                    hasAlarm = hasAlarm
                )
                val newId = repository.addReminder(newReminder)
                if (hasAlarm) {
                    AlarmScheduler.scheduleReminder(getApplication(), newReminder.copy(id = newId))
                }
            }
            closeReminderDialog()
        }
    }

    fun toggleReminderCompletion(reminder: ReminderItem) {
        viewModelScope.launch {
            val newCompleted = !reminder.isCompleted
            repository.toggleReminderCompletion(reminder.id, newCompleted)
            if (newCompleted) {
                AlarmScheduler.cancelReminder(getApplication(), reminder.id)
            } else if (reminder.hasAlarm) {
                AlarmScheduler.scheduleReminder(getApplication(), reminder.copy(isCompleted = false))
            }
        }
    }

    fun deleteReminder(id: Long) {
        viewModelScope.launch {
            AlarmScheduler.cancelReminder(getApplication(), id)
            repository.deleteReminder(id)
        }
    }

    companion object {
        fun getTodayDateString(): String {
            return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        }
    }
}
