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
import io.github.crazymisterno.GlucoseFit.data.proto.ActivityLevel
import io.github.crazymisterno.GlucoseFit.data.proto.GenderOption
import io.github.crazymisterno.GlucoseFit.data.proto.Goal
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
        weight: String?,
        heightFeet: String?,
        heightInches: String?,
        age: String?,
        gender: GenderOption?,
        activityLevel: ActivityLevel?,
        goal: Goal?,
        manualCalories: String?,
        insulinToCarbRatio: String?,
        correctionDose: String?,
        targetGlucose: String?,
        carbOnly: Boolean?,
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
        weight: String? = null,
        heightFeet: String? = null,
        heightInches: String? = null,
        age: String? = null,
        gender: GenderOption? = null,
        activityLevel: ActivityLevel? = null,
        goal: Goal? = null,
        manualCalories: String? = null,
        insulinToCarbRatio: String? = null,
        correctionDose: String? = null,
        targetGlucose: String? = null,
        carbOnly: Boolean? = null,
    )
}

fun GenderOption.format(): String {
    return when (this) {
        GenderOption.Male -> "Male"
        GenderOption.Female -> "Female"
        GenderOption.UNRECOGNIZED -> "<Select>"
    }
}

fun ActivityLevel.format(): String {
    return when (this) {
        ActivityLevel.Sedentary -> "Sedentary"
        ActivityLevel.LightlyActive -> "Lightly Active"
        ActivityLevel.Active -> "Active"
        ActivityLevel.VeryActive -> "Very Active"
        ActivityLevel.UNRECOGNIZED -> "<Select>"
    }
}

fun Goal.format(): String {
    return when (this) {
        Goal.Maintain -> "Maintain Weight"
        Goal.LoseOne -> "Lose 1lb a week"
        Goal.GainOne -> "Gain 1lb a week"
        Goal.UNRECOGNIZED -> "<Select>"
    }
}