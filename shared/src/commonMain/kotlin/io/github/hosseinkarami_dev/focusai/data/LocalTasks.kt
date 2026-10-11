package io.github.hosseinkarami_dev.focusai.data

import androidx.room3.Dao
import androidx.room3.Database
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.room3.ConstructedBy
import androidx.room3.AutoMigration
import androidx.room3.ColumnInfo
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.Upsert
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import io.github.hosseinkarami_dev.focusai.currentTimeMillis

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val details: String = "",
    val priority: String = "Medium",
    val estimateMinutes: Int = 25,
    val completed: Boolean = false,
    val createdAtEpochMillis: Long,
    @ColumnInfo(defaultValue = "0") val planOrder: Int = 0,
    val dueDateIso: String? = null,
    val completedAtEpochMillis: Long? = null,
    val recurrenceRule: String? = null,
    val spawnedFromTaskId: String? = null,
    val reminderTimeLocal: String? = null
)

@Entity(
    tableName = "subtasks",
    foreignKeys = [ForeignKey(entity = TaskEntity::class, parentColumns = ["id"], childColumns = ["taskId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("taskId")]
)
data class SubtaskEntity(
    @PrimaryKey val id: String,
    val taskId: String,
    val title: String,
    val completed: Boolean = false,
    val position: Int = 0
)

data class TaskStepDraft(val id: String?, val title: String, val completed: Boolean)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: String,
    val body: String,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long,
    val processed: Boolean = false,
    @ColumnInfo(defaultValue = "0") val archived: Boolean = false
)

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey val id: String,
    val taskId: String? = null,
    val plannedDurationSeconds: Long,
    val startedAtEpochMillis: Long,
    val accumulatedRunningMillis: Long = 0,
    val lastResumedAtEpochMillis: Long? = null,
    val endedAtEpochMillis: Long? = null,
    val outcome: String = "RUNNING"
)

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY completed ASC, planOrder ASC, createdAtEpochMillis ASC")
    fun observeAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE completed = 0 ORDER BY planOrder ASC, createdAtEpochMillis ASC")
    suspend fun getOpenTasksInPlanOrder(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE completed = 0 ORDER BY CASE WHEN dueDateIso IS NULL THEN 1 ELSE 0 END, dueDateIso ASC, planOrder ASC, createdAtEpochMillis ASC LIMIT 3")
    suspend fun getWidgetTasks(): List<TaskEntity>

    @Query("SELECT COALESCE(MAX(planOrder), -1) + 1 FROM tasks WHERE completed = 0")
    suspend fun nextPlanOrder(): Int

    @Query("SELECT * FROM tasks WHERE completed = 0 AND dueDateIso IS NOT NULL AND reminderTimeLocal IS NOT NULL")
    suspend fun getOpenTasksWithReminders(): List<TaskEntity>

    @Query("UPDATE tasks SET planOrder = :order WHERE id = :id")
    suspend fun setPlanOrder(id: String, order: Int)

    @Transaction
    suspend fun reorderOpenTasks(idsInOrder: List<String>) {
        idsInOrder.forEachIndexed { index, id -> setPlanOrder(id, index) }
    }

    @Upsert
    suspend fun upsert(task: TaskEntity)

    @Query("UPDATE tasks SET completed = :completed, completedAtEpochMillis = :completedAt WHERE id = :id")
    suspend fun setCompleted(id: String, completed: Boolean, completedAt: Long?)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM tasks WHERE spawnedFromTaskId = :taskId LIMIT 1")
    suspend fun getNextOccurrence(taskId: String): TaskEntity?
}

@Dao
interface SubtaskDao {
    @Query("SELECT * FROM subtasks WHERE taskId = :taskId ORDER BY position ASC")
    suspend fun getForTask(taskId: String): List<SubtaskEntity>

    @Upsert
    suspend fun upsertAll(subtasks: List<SubtaskEntity>)

    @Query("DELETE FROM subtasks WHERE taskId = :taskId")
    suspend fun deleteForTask(taskId: String)

    @Transaction
    suspend fun replaceForTask(taskId: String, subtasks: List<SubtaskEntity>) {
        deleteForTask(taskId)
        if (subtasks.isNotEmpty()) upsertAll(subtasks)
    }
}

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY updatedAtEpochMillis DESC")
    fun observeAll(): Flow<List<NoteEntity>>

    @Upsert
    suspend fun upsert(note: NoteEntity)

    @Query("UPDATE notes SET processed = :processed, updatedAtEpochMillis = :updatedAt WHERE id = :id")
    suspend fun setProcessed(id: String, processed: Boolean, updatedAt: Long)

    @Query("UPDATE notes SET archived = :archived, updatedAtEpochMillis = :updatedAt WHERE id = :id")
    suspend fun setArchived(id: String, archived: Boolean, updatedAt: Long)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun delete(id: String)
}

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions WHERE endedAtEpochMillis IS NULL ORDER BY startedAtEpochMillis DESC LIMIT 1")
    fun observeActive(): Flow<FocusSessionEntity?>

    @Query("SELECT * FROM focus_sessions WHERE outcome != 'DISCARDED' ORDER BY startedAtEpochMillis DESC LIMIT 20")
    fun observeHistory(): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions ORDER BY startedAtEpochMillis DESC")
    fun observeAll(): Flow<List<FocusSessionEntity>>

    @Upsert suspend fun upsert(session: FocusSessionEntity)
    @Query("SELECT * FROM focus_sessions WHERE endedAtEpochMillis IS NULL ORDER BY startedAtEpochMillis DESC LIMIT 1")
    suspend fun getActive(): FocusSessionEntity?
}

@Database(
    entities = [TaskEntity::class, NoteEntity::class, SubtaskEntity::class, AppSettingsEntity::class, FocusSessionEntity::class],
    version = 12,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2), AutoMigration(from = 2, to = 3), AutoMigration(from = 3, to = 4), AutoMigration(from = 4, to = 5), AutoMigration(from = 5, to = 6), AutoMigration(from = 6, to = 7), AutoMigration(from = 7, to = 8), AutoMigration(from = 8, to = 9), AutoMigration(from = 9, to = 10), AutoMigration(from = 10, to = 11), AutoMigration(from = 11, to = 12)]
)
@ConstructedBy(FocusDatabaseConstructor::class)
abstract class FocusDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun subtaskDao(): SubtaskDao
    abstract fun noteDao(): NoteDao
    abstract fun settingsDao(): SettingsDao
    abstract fun focusSessionDao(): FocusSessionDao
}

expect suspend fun loadWidgetTaskTitles(): List<String>

@Suppress("KotlinNoActualForExpect")
expect object FocusDatabaseConstructor : RoomDatabaseConstructor<FocusDatabase> {
    override fun initialize(): FocusDatabase
}

fun createFocusDatabase(builder: RoomDatabase.Builder<FocusDatabase>): FocusDatabase =
    builder.setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default)
        .build()


interface TaskRepository {
    fun observeTasks(): Flow<List<TaskEntity>>
    suspend fun saveTask(title: String, details: String = "", priority: String = "Medium", estimateMinutes: Int = 25, dueDateIso: String? = null, recurrenceRule: String? = null, reminderTimeLocal: String? = null, subtasks: List<TaskStepDraft> = emptyList(), existing: TaskEntity? = null): Boolean
    suspend fun getSubtasks(taskId: String): List<SubtaskEntity>
    suspend fun setCompleted(task: TaskEntity, completed: Boolean)
    suspend fun deleteTask(id: String)
    suspend fun moveTask(id: String, direction: Int)
}

class RoomTaskRepository(private val dao: TaskDao, private val subtaskDao: SubtaskDao, private val reminderScheduler: io.github.hosseinkarami_dev.focusai.TaskReminderScheduler) : TaskRepository {
    override fun observeTasks(): Flow<List<TaskEntity>> = dao.observeAll()

    override suspend fun saveTask(title: String, details: String, priority: String, estimateMinutes: Int, dueDateIso: String?, recurrenceRule: String?, reminderTimeLocal: String?, subtasks: List<TaskStepDraft>, existing: TaskEntity?): Boolean {
        val saved = TaskEntity(
                id = existing?.id ?: "task-${currentTimeMillis()}-$title".hashCode().toString(),
                title = title.trim(),
                details = details.trim(),
                priority = priority,
                estimateMinutes = estimateMinutes.coerceIn(5, 1440),
                completed = existing?.completed ?: false,
                createdAtEpochMillis = existing?.createdAtEpochMillis ?: currentTimeMillis(),
                planOrder = existing?.planOrder ?: dao.nextPlanOrder(),
                dueDateIso = dueDateIso,
                completedAtEpochMillis = existing?.completedAtEpochMillis,
                recurrenceRule = recurrenceRule,
                spawnedFromTaskId = existing?.spawnedFromTaskId,
                reminderTimeLocal = reminderTimeLocal
            )
        dao.upsert(saved)
        subtaskDao.replaceForTask(saved.id, subtasks.filter { it.title.isNotBlank() }.mapIndexed { index, step ->
            SubtaskEntity(
                id = step.id ?: "${saved.id}-step-${currentTimeMillis()}-$index-${step.title.hashCode()}",
                taskId = saved.id,
                title = step.title.trim(),
                completed = step.completed,
                position = index
            )
        })
        reminderScheduler.cancel(saved.id)
        return if (saved.completed || saved.dueDateIso == null || saved.reminderTimeLocal == null) true
        else reminderScheduler.schedule(saved.id, saved.title, saved.dueDateIso, saved.reminderTimeLocal)
    }

    override suspend fun getSubtasks(taskId: String): List<SubtaskEntity> = subtaskDao.getForTask(taskId)

    override suspend fun setCompleted(task: TaskEntity, completed: Boolean) {
        dao.setCompleted(task.id, completed, if (completed) currentTimeMillis() else null)
        if (completed) {
            reminderScheduler.cancel(task.id)
            val nextDate = task.dueDateIso?.let { io.github.hosseinkarami_dev.focusai.nextDueDate(it, task.recurrenceRule) }
            if (nextDate != null && task.recurrenceRule != null) {
                val nextId = "${task.id}-next-${nextDate}"
                val next = task.copy(
                    id = nextId,
                    completed = false,
                    createdAtEpochMillis = currentTimeMillis(),
                    planOrder = dao.nextPlanOrder(),
                    dueDateIso = nextDate,
                    completedAtEpochMillis = null,
                    spawnedFromTaskId = task.id
                )
                dao.upsert(next)
                val nextSteps = subtaskDao.getForTask(task.id).mapIndexed { index, step ->
                    step.copy(id = "$nextId-step-$index", taskId = nextId, completed = false)
                }
                subtaskDao.replaceForTask(nextId, nextSteps)
                next.reminderTimeLocal?.let { reminderScheduler.schedule(next.id, next.title, nextDate, it) }
            }
        } else {
            deleteOccurrenceChain(task.id)
            if (task.dueDateIso != null && task.reminderTimeLocal != null) reminderScheduler.schedule(task.id, task.title, task.dueDateIso, task.reminderTimeLocal)
        }
    }

    override suspend fun deleteTask(id: String) {
        reminderScheduler.cancel(id)
        deleteOccurrenceChain(id)
        dao.delete(id)
    }

    private suspend fun deleteOccurrenceChain(taskId: String) {
        val next = dao.getNextOccurrence(taskId) ?: return
        reminderScheduler.cancel(next.id)
        deleteOccurrenceChain(next.id)
        dao.delete(next.id)
    }

    override suspend fun moveTask(id: String, direction: Int) {
        val tasks = dao.getOpenTasksInPlanOrder()
        val currentIndex = tasks.indexOfFirst { it.id == id }
        val targetIndex = currentIndex + direction.coerceIn(-1, 1)
        if (currentIndex < 0 || targetIndex !in tasks.indices) return
        val updated = tasks.toMutableList().apply {
            add(targetIndex, removeAt(currentIndex))
        }
        dao.reorderOpenTasks(updated.map { it.id })
    }
}

interface NoteRepository {
    fun observeNotes(): Flow<List<NoteEntity>>
    suspend fun saveNote(body: String, existing: NoteEntity? = null): NoteEntity
    suspend fun restoreNote(note: NoteEntity)
    suspend fun markProcessed(id: String)
    suspend fun setArchived(id: String, archived: Boolean)
    suspend fun deleteNote(id: String)
}

class RoomNoteRepository(private val dao: NoteDao) : NoteRepository {
    override fun observeNotes(): Flow<List<NoteEntity>> = dao.observeAll()

    override suspend fun saveNote(body: String, existing: NoteEntity?): NoteEntity {
        val now = currentTimeMillis()
        return NoteEntity(
            id = existing?.id ?: "note-$now-${body.hashCode()}",
            body = body.trim(),
            createdAtEpochMillis = existing?.createdAtEpochMillis ?: now,
            updatedAtEpochMillis = now,
            processed = existing?.processed ?: false
        ).also { dao.upsert(it) }
    }

    override suspend fun restoreNote(note: NoteEntity) {
        dao.upsert(note)
    }

    override suspend fun markProcessed(id: String) {
        dao.setProcessed(id, true, currentTimeMillis())
    }

    override suspend fun setArchived(id: String, archived: Boolean) {
        dao.setArchived(id, archived, currentTimeMillis())
    }

    override suspend fun deleteNote(id: String) {
        dao.delete(id)
    }
}

interface FocusSessionRepository {
    fun observeActive(): Flow<FocusSessionEntity?>
    fun observeHistory(): Flow<List<FocusSessionEntity>>
    fun observeAll(): Flow<List<FocusSessionEntity>>
    suspend fun start(durationMinutes: Int, taskId: String?)
    suspend fun pause()
    suspend fun resume()
    suspend fun finish(): Boolean
    suspend fun discard()
}

class RoomFocusSessionRepository(private val dao: FocusSessionDao) : FocusSessionRepository {
    override fun observeActive() = dao.observeActive()
    override fun observeHistory() = dao.observeHistory()
    override fun observeAll() = dao.observeAll()
    override suspend fun start(durationMinutes: Int, taskId: String?) {
        if (dao.getActive() != null) return
        val now = currentTimeMillis()
        dao.upsert(FocusSessionEntity("focus-$now", taskId, durationMinutes.coerceIn(5, 120) * 60L, now, lastResumedAtEpochMillis = now))
    }
    override suspend fun pause() {
        val session = dao.getActive() ?: return
        val resumed = session.lastResumedAtEpochMillis ?: return
        val now = currentTimeMillis()
        dao.upsert(session.copy(accumulatedRunningMillis = session.accumulatedRunningMillis + (now - resumed).coerceAtLeast(0), lastResumedAtEpochMillis = null, outcome = "PAUSED"))
    }
    override suspend fun resume() {
        val session = dao.getActive() ?: return
        if (session.lastResumedAtEpochMillis != null) return
        dao.upsert(session.copy(lastResumedAtEpochMillis = currentTimeMillis(), outcome = "RUNNING"))
    }
    override suspend fun finish(): Boolean {
        val session = dao.getActive() ?: return false
        val now = currentTimeMillis()
        val extra = session.lastResumedAtEpochMillis?.let { (now - it).coerceAtLeast(0) } ?: 0L
        dao.upsert(session.copy(accumulatedRunningMillis = session.accumulatedRunningMillis + extra, lastResumedAtEpochMillis = null, endedAtEpochMillis = now, outcome = "COMPLETED"))
        return true
    }
    override suspend fun discard() {
        val session = dao.getActive() ?: return
        dao.upsert(session.copy(lastResumedAtEpochMillis = null, endedAtEpochMillis = currentTimeMillis(), outcome = "DISCARDED"))
    }
}

