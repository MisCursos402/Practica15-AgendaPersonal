package com.miscursos.practica15git.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miscursos.practica15git.ui.agenda.AddEditEventDialog
import com.miscursos.practica15git.ui.agenda.AgendaScreen
import com.miscursos.practica15git.ui.components.SearchBarField
import com.miscursos.practica15git.ui.notes.AddEditNoteDialog
import com.miscursos.practica15git.ui.notes.NotesScreen
import com.miscursos.practica15git.ui.reminders.AddEditReminderDialog
import com.miscursos.practica15git.ui.reminders.RemindersScreen

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val currentTab by viewModel.currentTab.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val (todayEventsCount, totalNotesCount, pendingRemindersCount) = viewModel.stats.collectAsState().value

    // Agenda state
    val events by viewModel.filteredEvents.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val filterByDate by viewModel.filterBySelectedDate.collectAsState()
    val showEventDialog by viewModel.showEventDialog.collectAsState()
    val editingEvent by viewModel.editingEvent.collectAsState()

    // Notes state
    val notes by viewModel.filteredNotes.collectAsState()
    val noteCategories by viewModel.noteCategories.collectAsState()
    val selectedNoteCategory by viewModel.selectedNoteCategory.collectAsState()
    val showNoteDialog by viewModel.showNoteDialog.collectAsState()
    val editingNote by viewModel.editingNote.collectAsState()

    // Reminders state
    val reminders by viewModel.filteredReminders.collectAsState()
    val reminderFilter by viewModel.reminderFilter.collectAsState()
    val showReminderDialog by viewModel.showReminderDialog.collectAsState()
    val editingReminder by viewModel.editingReminder.collectAsState()

    // Notification permission request for Android 13+
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { _ -> }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        topBar = {
            TopHeaderSection(
                currentTab = currentTab,
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                todayEvents = todayEventsCount,
                totalNotes = totalNotesCount,
                pendingReminders = pendingRemindersCount
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentTab == AppTab.AGENDA,
                    onClick = { viewModel.setTab(AppTab.AGENDA) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (todayEventsCount > 0) {
                                    Badge { Text(todayEventsCount.toString()) }
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Agenda")
                        }
                    },
                    label = { Text("Agenda") }
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.NOTAS,
                    onClick = { viewModel.setTab(AppTab.NOTAS) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (totalNotesCount > 0) {
                                    Badge { Text(totalNotesCount.toString()) }
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Description, contentDescription = "Notas")
                        }
                    },
                    label = { Text("Notas") }
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.RECORDATORIOS,
                    onClick = { viewModel.setTab(AppTab.RECORDATORIOS) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (pendingRemindersCount > 0) {
                                    Badge { Text(pendingRemindersCount.toString()) }
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Recordatorios")
                        }
                    },
                    label = { Text("Recordatorios") }
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    when (currentTab) {
                        AppTab.AGENDA -> viewModel.openAddEventDialog()
                        AppTab.NOTAS -> viewModel.openAddNoteDialog()
                        AppTab.RECORDATORIOS -> viewModel.openAddReminderDialog()
                    }
                },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = {
                    Text(
                        when (currentTab) {
                            AppTab.AGENDA -> "Evento"
                            AppTab.NOTAS -> "Nota"
                            AppTab.RECORDATORIOS -> "Recordatorio"
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = currentTab,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "TabContentAnimation",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { tab ->
            when (tab) {
                AppTab.AGENDA -> {
                    AgendaScreen(
                        events = events,
                        selectedDate = selectedDate,
                        filterByDate = filterByDate,
                        onDateSelected = { viewModel.setSelectedDate(it) },
                        onToggleFilterByDate = { viewModel.toggleFilterByDate(it) },
                        onEditEvent = { viewModel.openEditEventDialog(it) },
                        onDeleteEvent = { viewModel.deleteEvent(it) }
                    )
                }

                AppTab.NOTAS -> {
                    NotesScreen(
                        notes = notes,
                        categories = noteCategories,
                        selectedCategory = selectedNoteCategory,
                        onCategorySelected = { viewModel.setSelectedNoteCategory(it) },
                        onEditNote = { viewModel.openEditNoteDialog(it) },
                        onDeleteNote = { viewModel.deleteNote(it) },
                        onTogglePin = { viewModel.toggleNotePin(it) }
                    )
                }

                AppTab.RECORDATORIOS -> {
                    RemindersScreen(
                        reminders = reminders,
                        currentFilter = reminderFilter,
                        onFilterSelected = { viewModel.setReminderFilter(it) },
                        onToggleCompletion = { viewModel.toggleReminderCompletion(it) },
                        onEditReminder = { viewModel.openEditReminderDialog(it) },
                        onDeleteReminder = { viewModel.deleteReminder(it) }
                    )
                }
            }
        }
    }

    // Dialogs
    if (showEventDialog) {
        AddEditEventDialog(
            initialEvent = editingEvent,
            defaultDate = selectedDate,
            onDismiss = { viewModel.closeEventDialog() }
        ) { title, desc, date, time, location, category, priority ->
            viewModel.saveEvent(title, desc, date, time, location, category, priority)
        }
    }

    if (showNoteDialog) {
        AddEditNoteDialog(
            initialNote = editingNote,
            existingCategories = noteCategories,
            onDismiss = { viewModel.closeNoteDialog() }
        ) { title, content, category, colorHex ->
            viewModel.saveNote(title, content, category, colorHex)
        }
    }

    if (showReminderDialog) {
        AddEditReminderDialog(
            initialReminder = editingReminder,
            defaultDate = MainViewModel.getTodayDateString(),
            onDismiss = { viewModel.closeReminderDialog() }
        ) { title, desc, date, time, priority, hasAlarm ->
            viewModel.saveReminder(title, desc, date, time, priority, hasAlarm)
        }
    }
}

@Composable
fun TopHeaderSection(
    currentTab: AppTab,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    todayEvents: Int,
    totalNotes: Int,
    pendingReminders: Int
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Agenda & Notas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = when (currentTab) {
                            AppTab.AGENDA -> "Organiza tu día a día"
                            AppTab.NOTAS -> "Tus notas y apuntes"
                            AppTab.RECORDATORIOS -> "Tareas pendientes"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Quick stats row
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    QuickStatBadge(
                        icon = Icons.Default.Today,
                        count = todayEvents,
                        label = "Hoy"
                    )
                    QuickStatBadge(
                        icon = Icons.AutoMirrored.Filled.StickyNote2,
                        count = totalNotes,
                        label = "Notas"
                    )
                    QuickStatBadge(
                        icon = Icons.Default.Notifications,
                        count = pendingReminders,
                        label = "Pendientes"
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            SearchBarField(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                placeholderText = when (currentTab) {
                    AppTab.AGENDA -> "Buscar eventos o lugares..."
                    AppTab.NOTAS -> "Buscar en notas..."
                    AppTab.RECORDATORIOS -> "Buscar tareas o recordatorios..."
                }
            )
        }
    }
}

@Composable
fun QuickStatBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: Int,
    label: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.height(30.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "$count",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
