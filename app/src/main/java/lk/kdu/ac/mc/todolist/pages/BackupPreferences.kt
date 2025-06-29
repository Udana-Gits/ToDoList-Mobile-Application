package lk.kdu.ac.mc.todolist.pages

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class BackupPreferences(private val context: Context) {
    companion object {
        val BACKUP_KEY = booleanPreferencesKey("backup_enabled")
    }

    val backupEnabledFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[BACKUP_KEY] ?: false }

    suspend fun setBackupEnabled(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[BACKUP_KEY] = value
        }
    }
}
