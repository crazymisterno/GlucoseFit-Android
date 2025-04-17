package io.github.crazymisterno.GlucoseFit.presentation.data

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class MessagingViewModel @Inject constructor(
    private val delegate: MessagingManager
): ViewModel() {
    fun logNewFood(item: FoodItem) {
        val data = Json.encodeToString(item).encodeToByteArray()
        delegate.sendMessage("/log/new", data)
    }

    fun logSavedFood(item: SavedFoodItem) {
        val data = Json.encodeToString(item).encodeToByteArray()
        delegate.sendMessage("/log/import", data)
    }

    fun logDose(units: Double) {
        val entry = DoseLogEntry(units, LocalTime.now())
        val data = Json.encodeToString(entry).encodeToByteArray()
        delegate.sendMessage("/log/dose", data)
    }

    suspend fun requestMeals(): List<MealLogEntry> {
        return delegate.makeMealRequest()
    }

    suspend fun requestSavedFood(): List<SavedFoodItem> {
        return delegate.makeSavedFoodRequest()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RequestingDI {
    @Provides
    @Singleton
    fun provideMessagingClient(@ApplicationContext context: Context): MessageClient {
        return Wearable.getMessageClient(context)
    }

    @Provides
    @Singleton
    fun provideDataClient(@ApplicationContext context: Context): DataClient {
        return Wearable.getDataClient(context)
    }

    @Provides
    @Singleton
    fun provideNodeClient(@ApplicationContext context: Context): NodeClient {
        return Wearable.getNodeClient(context)
    }
}