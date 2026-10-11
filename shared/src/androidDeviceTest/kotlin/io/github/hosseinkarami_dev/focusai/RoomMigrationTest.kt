package io.github.hosseinkarami_dev.focusai

import androidx.room3.testing.MigrationTestHelper
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.hosseinkarami_dev.focusai.data.FocusDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class RoomMigrationTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val databaseName = "focusai-migration-test.db"

    @get:Rule
    val helper = MigrationTestHelper(
        instrumentation = instrumentation,
        databaseClass = FocusDatabase::class,
        driver = AndroidSQLiteDriver(),
        file = instrumentation.targetContext.getDatabasePath(databaseName)
    )

    @Before
    fun removePreviousFixture() {
        instrumentation.targetContext.deleteDatabase(databaseName)
    }

    @Test
    fun migratesEveryReleasedVersionToCurrentSchema() = runTest {
        helper.createDatabase(1).close()

        val migrated = helper.runMigrationsAndValidate(12, emptyList())
        migrated.prepare("SELECT dailyFocusGoalMinutes, breakRemindersEnabled FROM app_settings").use { statement ->
            // A new database has no settings row until the repository creates its defaults.
            assertTrue(statement.step().not())
        }
        migrated.close()
    }

    @Test
    fun versionElevenUpgradePreservesSettingsAndAddsDefaults() = runTest {
        helper.createDatabase(11).use { connection ->
            connection.prepare(
                "INSERT INTO app_settings (id, themeMode, focusLengthMinutes, language) " +
                    "VALUES (1, 'Dark', 45, 'fr')"
            ).use { it.step() }
        }

        val migrated = helper.runMigrationsAndValidate(12, emptyList())
        migrated.prepare(
            "SELECT themeMode, focusLengthMinutes, language, dailyFocusGoalMinutes, breakRemindersEnabled " +
                "FROM app_settings WHERE id = 1"
        ).use { statement ->
            assertTrue(statement.step())
            assertEquals("Dark", statement.getText(0))
            assertEquals(45L, statement.getLong(1))
            assertEquals("fr", statement.getText(2))
            assertEquals(120L, statement.getLong(3))
            assertEquals(0L, statement.getLong(4))
        }
        migrated.close()
    }
}
