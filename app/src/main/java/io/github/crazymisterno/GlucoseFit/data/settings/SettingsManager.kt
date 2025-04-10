package io.github.crazymisterno.GlucoseFit.data.settings

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import io.github.crazymisterno.GlucoseFit.data.proto.Settings
import com.google.protobuf.InvalidProtocolBufferException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

object SettingsSerializer : Serializer<Settings> {
    override val defaultValue: Settings
        get() = Settings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Settings {
        try {
            return Settings.parseFrom(input)
        } catch (ex: InvalidProtocolBufferException) {
            throw CorruptionException("Data corrupted", ex)
        }
    }

    override suspend fun writeTo(t: Settings, output: OutputStream) {
        t.writeTo(output)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SettingsDI {
    @Provides
    @Singleton
    fun settings(@ApplicationContext context: Context): SettingsProvider {
        val store = DataStoreFactory.create(
            serializer = SettingsSerializer,
            produceFile = { context.dataStoreFile("settings.pb") }
        )
        return SettingsDataProvider(store)
    }
}

class SettingsDataProvider @Inject constructor(
    private val dataStore: DataStore<Settings>
) : SettingsProvider {
    override val shared: Flow<Settings> = dataStore.data.map { preferences ->
        preferences
    }

    override suspend fun updateSettings(
        manualCalories: String?,
        carbOnly: Boolean?,
        setupComplete: Boolean?
    ) {
        dataStore.updateData { preferences ->
            var builder = preferences.toBuilder()
            if (manualCalories != null)
                builder.setManualCalories(manualCalories)
            if (carbOnly != null)
                builder.setCarbOnly(carbOnly)
            if (setupComplete != null)
                builder.setSetUpComplete(setupComplete)
            builder.build()
        }
    }
}

interface SettingsProvider {
    val shared: Flow<Settings>

    suspend fun updateSettings(
        manualCalories: String? = null,
        carbOnly: Boolean? = null,
        setupComplete: Boolean? = null
    )
}