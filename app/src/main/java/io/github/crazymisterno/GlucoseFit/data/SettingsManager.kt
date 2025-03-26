package io.github.crazymisterno.GlucoseFit.data

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.codelab.android.datastore.Settings
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

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

val Context.settings: DataStore<Settings> by dataStore(
    fileName = "settings.pb",
    serializer = SettingsSerializer
)

class SettingsDataProvider(
    private val dataStore: DataStore<Settings>
) : SettingsProvider {
    override val shared: Flow<Settings> = dataStore.data.map { preferences ->
        preferences
    }

    override suspend fun updateSettings(
        weight: Double?,
        heightFeet: Double?,
        heightInches: Double?,
        age: Int?,
        gender: String?,
        activityLevel: String?,
        goal: String?,
        manualCalories: Double?,
        insulinToCarbRatio: Double?,
        correctionDose: Double?,
        targetGlucose: Double?,
        carbOnly: Boolean?
    ) {
        dataStore.updateData { preferences ->
            var builder = preferences.toBuilder()
            if (weight != null)
                builder.setWeight(weight)
            if (heightFeet != null)
                builder.setHeightFeet(heightFeet)
            if (heightInches != null)
                builder.setHeightInches(heightInches)
            if (age != null)
                builder.setAge(age)
            if (gender != null)
                builder.setGender(gender)
            if (activityLevel != null)
                builder.setActivityLevel(activityLevel)
            if (goal != null)
                builder.setGoal(goal)
            if (manualCalories != null)
                builder.setManualCalories(manualCalories)
            if (insulinToCarbRatio != null)
                builder.setInsulinToCarbRatio(insulinToCarbRatio)
            if (correctionDose != null)
                builder.setCorrectionDose(correctionDose)
            if (targetGlucose != null)
                builder.setTargetGlucose(targetGlucose)
            if (carbOnly != null)
                builder.setCarbOnly(carbOnly)
            builder.build()
        }
    }
}

interface SettingsProvider {
    val shared: Flow<Settings>

    suspend fun updateSettings(
        weight: Double? = null,
        heightFeet: Double? = null,
        heightInches: Double? = null,
        age: Int? = null,
        gender: String? = null,
        activityLevel: String? = null,
        goal: String? = null,
        manualCalories: Double? = null,
        insulinToCarbRatio: Double? = null,
        correctionDose: Double? = null,
        targetGlucose: Double? = null,
        carbOnly: Boolean? = null
    )
}