package io.github.hosseinkarami_dev.focusai.data

import androidx.room3.Dao
import androidx.room3.Database
import androidx.room3.Entity
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.PrimaryKey
import androidx.room3.Query
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.room3.ConstructedBy
import androidx.room3.Room
import androidx.room3.Upsert
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val details: String = "",
    val priority: String = "Medium",
    val estimateMinutes: Int = 25,
    val completed: Boolean = false,
    val createdAtEpochMillis: Long
)

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY completed ASC, createdAtEpochMillis ASC")
    fun observeAll(): Flow<List<TaskEntity>>

    @Upsert
    suspend fun upsert(task: TaskEntity)

    @Query("UPDATE tasks SET completed = :completed WHERE id = :id")
    suspend fun setCompleted(id: String, completed: Boolean)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun delete(id: String)
}

@Database(entities = [TaskEntity::class], version = 1, exportSchema = true)
@ConstructedBy(FocusDatabaseConstructor::class)
abstract class FocusDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

@Suppress("KotlinNoActualForExpect")
expect object FocusDatabaseConstructor : RoomDatabaseConstructor<FocusDatabase> {
    override fun initialize(): FocusDatabase
}

fun createFocusDatabase(builder: RoomDatabase.Builder<FocusDatabase>): FocusDatabase =
    builder.setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()


interface TaskRepository {
    fun observeTasks(): Flow<List<TaskEntity>>
    suspend fun addTask(title: String, details: String = "", priority: String = "Medium", estimateMinutes: Int = 25)
    suspend fun setCompleted(task: TaskEntity, completed: Boolean)
}

class RoomTaskRepository(private val dao: TaskDao) : TaskRepository {
    override fun observeTasks(): Flow<List<TaskEntity>> = dao.observeAll()

    override suspend fun addTask(title: String, details: String, priority: String, estimateMinutes: Int) {
        dao.upsert(
            TaskEntity(
                id = "task-${currentTimeMillis()}-$title".hashCode().toString(),
                title = title.trim(),
                details = details.trim(),
                priority = priority,
                estimateMinutes = estimateMinutes.coerceIn(5, 1440),
                createdAtEpochMillis = currentTimeMillis()
            )
        )
    }

    override suspend fun setCompleted(task: TaskEntity, completed: Boolean) {
        dao.setCompleted(task.id, completed)
    }
}

