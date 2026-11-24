package com.miscursos.practica15git.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.miscursos.practica15git.data.model.EventItem
import com.miscursos.practica15git.data.model.NoteItem
import com.miscursos.practica15git.data.model.Priority
import com.miscursos.practica15git.data.model.ReminderItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "agenda_notes_app.db"
        const val DATABASE_VERSION = 1

        // Table Events
        const val TABLE_EVENTS = "events"
        const val COL_EVENT_ID = "id"
        const val COL_EVENT_TITLE = "title"
        const val COL_EVENT_DESC = "description"
        const val COL_EVENT_DATE = "date"
        const val COL_EVENT_TIME = "time"
        const val COL_EVENT_LOCATION = "location"
        const val COL_EVENT_CATEGORY = "category"
        const val COL_EVENT_PRIORITY = "priority"
        const val COL_EVENT_CREATED_AT = "created_at"

        // Table Notes
        const val TABLE_NOTES = "notes"
        const val COL_NOTE_ID = "id"
        const val COL_NOTE_TITLE = "title"
        const val COL_NOTE_CONTENT = "content"
        const val COL_NOTE_CATEGORY = "category"
        const val COL_NOTE_COLOR = "color_hex"
        const val COL_NOTE_IS_PINNED = "is_pinned"
        const val COL_NOTE_CREATED_AT = "created_at"
        const val COL_NOTE_UPDATED_AT = "updated_at"

        // Table Reminders
        const val TABLE_REMINDERS = "reminders"
        const val COL_REM_ID = "id"
        const val COL_REM_TITLE = "title"
        const val COL_REM_DESC = "description"
        const val COL_REM_DATE = "date"
        const val COL_REM_TIME = "time"
        const val COL_REM_PRIORITY = "priority"
        const val COL_REM_IS_COMPLETED = "is_completed"
        const val COL_REM_HAS_ALARM = "has_alarm"
        const val COL_REM_CREATED_AT = "created_at"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createEventsTable = """
            CREATE TABLE $TABLE_EVENTS (
                $COL_EVENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_EVENT_TITLE TEXT NOT NULL,
                $COL_EVENT_DESC TEXT,
                $COL_EVENT_DATE TEXT NOT NULL,
                $COL_EVENT_TIME TEXT NOT NULL,
                $COL_EVENT_LOCATION TEXT,
                $COL_EVENT_CATEGORY TEXT,
                $COL_EVENT_PRIORITY TEXT,
                $COL_EVENT_CREATED_AT INTEGER
            )
        """.trimIndent()

        val createNotesTable = """
            CREATE TABLE $TABLE_NOTES (
                $COL_NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOTE_TITLE TEXT NOT NULL,
                $COL_NOTE_CONTENT TEXT NOT NULL,
                $COL_NOTE_CATEGORY TEXT,
                $COL_NOTE_COLOR INTEGER,
                $COL_NOTE_IS_PINNED INTEGER DEFAULT 0,
                $COL_NOTE_CREATED_AT INTEGER,
                $COL_NOTE_UPDATED_AT INTEGER
            )
        """.trimIndent()

        val createRemindersTable = """
            CREATE TABLE $TABLE_REMINDERS (
                $COL_REM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_REM_TITLE TEXT NOT NULL,
                $COL_REM_DESC TEXT,
                $COL_REM_DATE TEXT NOT NULL,
                $COL_REM_TIME TEXT NOT NULL,
                $COL_REM_PRIORITY TEXT,
                $COL_REM_IS_COMPLETED INTEGER DEFAULT 0,
                $COL_REM_HAS_ALARM INTEGER DEFAULT 1,
                $COL_REM_CREATED_AT INTEGER
            )
        """.trimIndent()

        db.execSQL(createEventsTable)
        db.execSQL(createNotesTable)
        db.execSQL(createRemindersTable)

        insertInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EVENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REMINDERS")
        onCreate(db)
    }

    private fun insertInitialData(db: SQLiteDatabase) {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Initial Events
        val event1 = ContentValues().apply {
            put(COL_EVENT_TITLE, "Reunión de Planificación")
            put(COL_EVENT_DESC, "Revisión de avances del proyecto y próximos entregables.")
            put(COL_EVENT_DATE, todayStr)
            put(COL_EVENT_TIME, "10:00")
            put(COL_EVENT_LOCATION, "Sala de Conferencias B / Google Meet")
            put(COL_EVENT_CATEGORY, "Trabajo")
            put(COL_EVENT_PRIORITY, Priority.ALTA.name)
            put(COL_EVENT_CREATED_AT, System.currentTimeMillis())
        }
        db.insert(TABLE_EVENTS, null, event1)

        val event2 = ContentValues().apply {
            put(COL_EVENT_TITLE, "Sesión de Estudio Android")
            put(COL_EVENT_DESC, "Aprender Jetpack Compose, Arquitectura MVVM y Base de Datos SQLite.")
            put(COL_EVENT_DATE, todayStr)
            put(COL_EVENT_TIME, "16:30")
            put(COL_EVENT_LOCATION, "Biblioteca Virtual")
            put(COL_EVENT_CATEGORY, "Estudio")
            put(COL_EVENT_PRIORITY, Priority.MEDIA.name)
            put(COL_EVENT_CREATED_AT, System.currentTimeMillis())
        }
        db.insert(TABLE_EVENTS, null, event2)

        // Initial Notes
        val note1 = ContentValues().apply {
            put(COL_NOTE_TITLE, "💡 Ideas para nuevas funciones")
            put(COL_NOTE_CONTENT, "1. Exportar notas a PDF\n2. Sincronización en la nube\n3. Widgets para pantalla de inicio\n4. Temas personalizados")
            put(COL_NOTE_CATEGORY, "Proyectos")
            put(COL_NOTE_COLOR, 0xFFFFF9C4) // pastel yellow
            put(COL_NOTE_IS_PINNED, 1)
            put(COL_NOTE_CREATED_AT, System.currentTimeMillis())
            put(COL_NOTE_UPDATED_AT, System.currentTimeMillis())
        }
        db.insert(TABLE_NOTES, null, note1)

        val note2 = ContentValues().apply {
            put(COL_NOTE_TITLE, "🛒 Lista de Compras y Suministros")
            put(COL_NOTE_CONTENT, "• Cuaderno de notas\n• Bolígrafos de tinta de gel\n• Café en grano\n• Adaptador USB-C")
            put(COL_NOTE_CATEGORY, "Personal")
            put(COL_NOTE_COLOR, 0xFFC8E6C9) // pastel green
            put(COL_NOTE_IS_PINNED, 0)
            put(COL_NOTE_CREATED_AT, System.currentTimeMillis())
            put(COL_NOTE_UPDATED_AT, System.currentTimeMillis())
        }
        db.insert(TABLE_NOTES, null, note2)

        val note3 = ContentValues().apply {
            put(COL_NOTE_TITLE, "📌 Atajos de Android Studio")
            put(COL_NOTE_CONTENT, "Shift + Shift: Buscar en todo\nCtrl + Alt + L: Formatear código\nCtrl + Espacio: Autocompletar\nAlt + Enter: Soluciones rápidas")
            put(COL_NOTE_CATEGORY, "Estudio")
            put(COL_NOTE_COLOR, 0xFFBBDEFB) // pastel blue
            put(COL_NOTE_IS_PINNED, 0)
            put(COL_NOTE_CREATED_AT, System.currentTimeMillis())
            put(COL_NOTE_UPDATED_AT, System.currentTimeMillis())
        }
        db.insert(TABLE_NOTES, null, note3)

        // Initial Reminders
        val rem1 = ContentValues().apply {
            put(COL_REM_TITLE, "Enviar reporte semanal de tareas")
            put(COL_REM_DESC, "Adjuntar métricas del equipo y resumen de commits.")
            put(COL_REM_DATE, todayStr)
            put(COL_REM_TIME, "18:00")
            put(COL_REM_PRIORITY, Priority.ALTA.name)
            put(COL_REM_IS_COMPLETED, 0)
            put(COL_REM_HAS_ALARM, 1)
            put(COL_REM_CREATED_AT, System.currentTimeMillis())
        }
        db.insert(TABLE_REMINDERS, null, rem1)

        val rem2 = ContentValues().apply {
            put(COL_REM_TITLE, "Tomar agua y descansar la vista")
            put(COL_REM_DESC, "Pausa activa cada 2 horas para cuidar la salud.")
            put(COL_REM_DATE, todayStr)
            put(COL_REM_TIME, "14:00")
            put(COL_REM_PRIORITY, Priority.BAJA.name)
            put(COL_REM_IS_COMPLETED, 1)
            put(COL_REM_HAS_ALARM, 0)
            put(COL_REM_CREATED_AT, System.currentTimeMillis())
        }
        db.insert(TABLE_REMINDERS, null, rem2)
    }

    // ================= EVENTS CRUD =================
    fun insertEvent(event: EventItem): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_EVENT_TITLE, event.title)
            put(COL_EVENT_DESC, event.description)
            put(COL_EVENT_DATE, event.date)
            put(COL_EVENT_TIME, event.time)
            put(COL_EVENT_LOCATION, event.location)
            put(COL_EVENT_CATEGORY, event.category)
            put(COL_EVENT_PRIORITY, event.priority.name)
            put(COL_EVENT_CREATED_AT, event.createdAt)
        }
        return db.insert(TABLE_EVENTS, null, values)
    }

    fun updateEvent(event: EventItem): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_EVENT_TITLE, event.title)
            put(COL_EVENT_DESC, event.description)
            put(COL_EVENT_DATE, event.date)
            put(COL_EVENT_TIME, event.time)
            put(COL_EVENT_LOCATION, event.location)
            put(COL_EVENT_CATEGORY, event.category)
            put(COL_EVENT_PRIORITY, event.priority.name)
        }
        return db.update(TABLE_EVENTS, values, "$COL_EVENT_ID = ?", arrayOf(event.id.toString()))
    }

    fun deleteEvent(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_EVENTS, "$COL_EVENT_ID = ?", arrayOf(id.toString()))
    }

    fun getAllEvents(): List<EventItem> {
        val list = mutableListOf<EventItem>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_EVENTS ORDER BY $COL_EVENT_DATE ASC, $COL_EVENT_TIME ASC",
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    EventItem(
                        id = it.getLong(it.getColumnIndexOrThrow(COL_EVENT_ID)),
                        title = it.getString(it.getColumnIndexOrThrow(COL_EVENT_TITLE)),
                        description = it.getString(it.getColumnIndexOrThrow(COL_EVENT_DESC)) ?: "",
                        date = it.getString(it.getColumnIndexOrThrow(COL_EVENT_DATE)),
                        time = it.getString(it.getColumnIndexOrThrow(COL_EVENT_TIME)),
                        location = it.getString(it.getColumnIndexOrThrow(COL_EVENT_LOCATION)) ?: "",
                        category = it.getString(it.getColumnIndexOrThrow(COL_EVENT_CATEGORY)) ?: "Personal",
                        priority = Priority.fromString(it.getString(it.getColumnIndexOrThrow(COL_EVENT_PRIORITY))),
                        createdAt = it.getLong(it.getColumnIndexOrThrow(COL_EVENT_CREATED_AT))
                    )
                )
            }
        }
        return list
    }

    // ================= NOTES CRUD =================
    fun insertNote(note: NoteItem): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOTE_TITLE, note.title)
            put(COL_NOTE_CONTENT, note.content)
            put(COL_NOTE_CATEGORY, note.category)
            put(COL_NOTE_COLOR, note.colorHex)
            put(COL_NOTE_IS_PINNED, if (note.isPinned) 1 else 0)
            put(COL_NOTE_CREATED_AT, note.createdAt)
            put(COL_NOTE_UPDATED_AT, note.updatedAt)
        }
        return db.insert(TABLE_NOTES, null, values)
    }

    fun updateNote(note: NoteItem): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOTE_TITLE, note.title)
            put(COL_NOTE_CONTENT, note.content)
            put(COL_NOTE_CATEGORY, note.category)
            put(COL_NOTE_COLOR, note.colorHex)
            put(COL_NOTE_IS_PINNED, if (note.isPinned) 1 else 0)
            put(COL_NOTE_UPDATED_AT, System.currentTimeMillis())
        }
        return db.update(TABLE_NOTES, values, "$COL_NOTE_ID = ?", arrayOf(note.id.toString()))
    }

    fun deleteNote(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_NOTES, "$COL_NOTE_ID = ?", arrayOf(id.toString()))
    }

    fun toggleNotePin(id: Long, isPinned: Boolean): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOTE_IS_PINNED, if (isPinned) 1 else 0)
            put(COL_NOTE_UPDATED_AT, System.currentTimeMillis())
        }
        return db.update(TABLE_NOTES, values, "$COL_NOTE_ID = ?", arrayOf(id.toString()))
    }

    fun getAllNotes(): List<NoteItem> {
        val list = mutableListOf<NoteItem>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NOTES ORDER BY $COL_NOTE_IS_PINNED DESC, $COL_NOTE_UPDATED_AT DESC",
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    NoteItem(
                        id = it.getLong(it.getColumnIndexOrThrow(COL_NOTE_ID)),
                        title = it.getString(it.getColumnIndexOrThrow(COL_NOTE_TITLE)),
                        content = it.getString(it.getColumnIndexOrThrow(COL_NOTE_CONTENT)),
                        category = it.getString(it.getColumnIndexOrThrow(COL_NOTE_CATEGORY)) ?: "General",
                        colorHex = it.getLong(it.getColumnIndexOrThrow(COL_NOTE_COLOR)),
                        isPinned = it.getInt(it.getColumnIndexOrThrow(COL_NOTE_IS_PINNED)) == 1,
                        createdAt = it.getLong(it.getColumnIndexOrThrow(COL_NOTE_CREATED_AT)),
                        updatedAt = it.getLong(it.getColumnIndexOrThrow(COL_NOTE_UPDATED_AT))
                    )
                )
            }
        }
        return list
    }

    // ================= REMINDERS CRUD =================
    fun insertReminder(reminder: ReminderItem): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_REM_TITLE, reminder.title)
            put(COL_REM_DESC, reminder.description)
            put(COL_REM_DATE, reminder.date)
            put(COL_REM_TIME, reminder.time)
            put(COL_REM_PRIORITY, reminder.priority.name)
            put(COL_REM_IS_COMPLETED, if (reminder.isCompleted) 1 else 0)
            put(COL_REM_HAS_ALARM, if (reminder.hasAlarm) 1 else 0)
            put(COL_REM_CREATED_AT, reminder.createdAt)
        }
        return db.insert(TABLE_REMINDERS, null, values)
    }

    fun updateReminder(reminder: ReminderItem): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_REM_TITLE, reminder.title)
            put(COL_REM_DESC, reminder.description)
            put(COL_REM_DATE, reminder.date)
            put(COL_REM_TIME, reminder.time)
            put(COL_REM_PRIORITY, reminder.priority.name)
            put(COL_REM_IS_COMPLETED, if (reminder.isCompleted) 1 else 0)
            put(COL_REM_HAS_ALARM, if (reminder.hasAlarm) 1 else 0)
        }
        return db.update(TABLE_REMINDERS, values, "$COL_REM_ID = ?", arrayOf(reminder.id.toString()))
    }

    fun toggleReminderCompletion(id: Long, isCompleted: Boolean): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_REM_IS_COMPLETED, if (isCompleted) 1 else 0)
        }
        return db.update(TABLE_REMINDERS, values, "$COL_REM_ID = ?", arrayOf(id.toString()))
    }

    fun deleteReminder(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_REMINDERS, "$COL_REM_ID = ?", arrayOf(id.toString()))
    }

    fun getAllReminders(): List<ReminderItem> {
        val list = mutableListOf<ReminderItem>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_REMINDERS ORDER BY $COL_REM_IS_COMPLETED ASC, $COL_REM_DATE ASC, $COL_REM_TIME ASC",
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    ReminderItem(
                        id = it.getLong(it.getColumnIndexOrThrow(COL_REM_ID)),
                        title = it.getString(it.getColumnIndexOrThrow(COL_REM_TITLE)),
                        description = it.getString(it.getColumnIndexOrThrow(COL_REM_DESC)) ?: "",
                        date = it.getString(it.getColumnIndexOrThrow(COL_REM_DATE)),
                        time = it.getString(it.getColumnIndexOrThrow(COL_REM_TIME)),
                        priority = Priority.fromString(it.getString(it.getColumnIndexOrThrow(COL_REM_PRIORITY))),
                        isCompleted = it.getInt(it.getColumnIndexOrThrow(COL_REM_IS_COMPLETED)) == 1,
                        hasAlarm = it.getInt(it.getColumnIndexOrThrow(COL_REM_HAS_ALARM)) == 1,
                        createdAt = it.getLong(it.getColumnIndexOrThrow(COL_REM_CREATED_AT))
                    )
                )
            }
        }
        return list
    }
}
