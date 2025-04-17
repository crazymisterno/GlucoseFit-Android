package io.github.crazymisterno.GlucoseFit.data.messaging

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.crazymisterno.GlucoseFit.data.storage.DataManager
import io.github.crazymisterno.GlucoseFit.data.storage.DoseLogEntry
import io.github.crazymisterno.GlucoseFit.data.storage.FoodItem
import io.github.crazymisterno.GlucoseFit.data.storage.InStorageDb
import io.github.crazymisterno.GlucoseFit.data.storage.MealLogEntry
import io.github.crazymisterno.GlucoseFit.data.storage.SavedFoodItem
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

class MessagingManager @Inject constructor(
    @InStorageDb
    private val database: DataManager,
    private val messenger: MessageClient,
): ViewModel(), MessageClient.OnMessageReceivedListener {
    init {
        Log.i("Mesage", "Listener has been initialized")
    }

    override fun onMessageReceived(p0: MessageEvent) {
        Log.println(Log.INFO, "Message", "Message received: ${p0.path}")
        when (p0.path) {
            "/log/new" -> {
                val food = Json.decodeFromString<FoodItem>(
                    p0.data.decodeToString()
                )
                viewModelScope.launch {
                    database.mealAccess().insertFood(food)
                }
            }
            "/log/import" -> {
                val food = Json.decodeFromString<SavedFoodItem>(
                    p0.data.decodeToString()
                )
                viewModelScope.launch {
                    val converted = FoodItem(
                        mealId = food.id,
                        name = food.name,
                        carbs = food.carbs,
                        calories = food.calories
                    )
                    database.mealAccess().insertFood(converted)
                }
            }
            "/log/dose" -> {
                val dose = Json.decodeFromString<DoseLogEntry>(
                    p0.data.decodeToString()
                )
                viewModelScope.launch {
                    database.doseLogAccess().newEntry(dose)
                }
            }
            "/request/meals" -> {
                viewModelScope.launch {
                    database.mealAccess().getByDate(LocalDate.now())
                        .collect { data ->
                            var blanks = mutableListOf<MealLogEntry>()
                            data.forEach {
                                blanks.add(it.meal)
                            }
                            val message = Json.encodeToString(blanks).encodeToByteArray()
                            messenger.sendMessage(p0.sourceNodeId, "/request/meals/ack", message)
                        }
                }
            }

            "/request/savedFood" -> {
                viewModelScope.launch {
                    database.savedFoodAccess().getAll()
                        .collect { data ->
                            val message = Json.encodeToString(data).encodeToByteArray()
                            messenger.sendMessage(p0.sourceNodeId, "/request/savedFood/ack", message)
                        }
                }
            }
            "/identify" -> {
                Log.println(Log.INFO, "Message", "Identification request received")
                val data = "phone".encodeToByteArray()
                messenger.sendMessage(p0.sourceNodeId, "/identify/ack", data)

            }
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ClientDI {
    @Provides
    @Singleton
    fun provideMessagingClient(@ApplicationContext context: Context): MessageClient {
        return Wearable.getMessageClient(context)
    }
}