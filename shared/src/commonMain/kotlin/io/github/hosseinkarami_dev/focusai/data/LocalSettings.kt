package io.github.hosseinkarami_dev.focusai.data

import androidx.room3.Dao
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import androidx.room3.Query
import androidx.room3.Upsert
import androidx.room3.ColumnInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val themeMode: String = "System",
    val focusLengthMinutes: Int = 25,
    @ColumnInfo(defaultValue = "'en'") val language: String = "en"
)

@Dao
interface SettingsDao {
    @Query("SELECT * FROM app_settings WHERE id = 1")
    fun observe(): Flow<AppSettingsEntity?>

    @Query("SELECT * FROM app_settings WHERE id = 1")
    suspend fun get(): AppSettingsEntity?

    @Upsert
    suspend fun upsert(settings: AppSettingsEntity)
}

interface SettingsRepository {
    fun observeSettings(): Flow<AppSettingsEntity>
    suspend fun setThemeMode(mode: String)
    suspend fun setFocusLength(minutes: Int)
    suspend fun setLanguage(language: String)
}

class RoomSettingsRepository(private val dao: SettingsDao) : SettingsRepository {
    override fun observeSettings(): Flow<AppSettingsEntity> = dao.observe().map { it ?: AppSettingsEntity() }

    override suspend fun setThemeMode(mode: String) {
        val current = dao.get() ?: AppSettingsEntity()
        dao.upsert(current.copy(themeMode = mode))
    }

    override suspend fun setFocusLength(minutes: Int) {
        val current = dao.get() ?: AppSettingsEntity()
        dao.upsert(current.copy(focusLengthMinutes = minutes.coerceIn(5, 120)))
    }

    override suspend fun setLanguage(language: String) {
        val current = dao.get() ?: AppSettingsEntity()
        dao.upsert(current.copy(language = language.takeIf { it in setOf("en", "fa", "es") } ?: "en"))
    }
}
