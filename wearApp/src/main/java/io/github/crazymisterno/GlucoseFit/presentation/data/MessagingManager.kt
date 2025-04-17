package io.github.crazymisterno.GlucoseFit.presentation.data

import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.NodeClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.job
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resumeWithException

@Singleton
class MessagingManager @Inject constructor(
    private val messenger: MessageClient,
    private val nodeLister: NodeClient
): MessageClient.OnMessageReceivedListener {
    private var phoneNode: String? = null

    init {
        Log.i("WearMessageHandler", "Created instance: $this")
    }

    override fun onMessageReceived(p0: MessageEvent) {
        Log.i("Message", "Message received: ${p0.path}")
        when (p0.path) {
            "/identify" -> {
                val data = "watch".encodeToByteArray()
                messenger.sendMessage(p0.sourceNodeId, "/identify/ack", data)
            }
            "/identify/ack" -> {
                if (p0.data.decodeToString() == "phone") {
                    phoneNode = p0.sourceNodeId
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun makeMealRequest(): List<MealLogEntry> = suspendCancellableCoroutine { cont ->
        if (phoneNode != null) {
            val listener = object : MessageClient.OnMessageReceivedListener {
                override fun onMessageReceived(p0: MessageEvent) {
                    if (p0.path == "/request/meals/ack") {
                        val meals =
                            Json.decodeFromString<List<MealLogEntry>>(p0.data.decodeToString())
                        cont.resume(meals) {
                            messenger.removeListener(this)
                        }
                    }
                }
            }

            messenger.addListener(listener)

            messenger.sendMessage(phoneNode!!, "/request/meals", "".encodeToByteArray())
                .addOnFailureListener { error ->
                    messenger.removeListener(listener)
                    if (cont.isActive) cont.resumeWithException(error)
                }

            cont.invokeOnCancellation {
                messenger.removeListener(listener)
            }
            cont.context.job.invokeOnCompletion { cause ->
                if (cause is TimeoutCancellationException && cont.isActive) {
                    cont.resumeWithException(cause)
                }
                messenger.removeListener(listener)
            }
        } else
            cont.resumeWithException(NullPointerException("No phone has been identified"))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun makeSavedFoodRequest(): List<SavedFoodItem> = suspendCancellableCoroutine { cont ->
        if (phoneNode != null) {
            val listener = object : MessageClient.OnMessageReceivedListener {
                override fun onMessageReceived(p0: MessageEvent) {
                    if (p0.path == "/request/savedFood/ack") {
                        val foods =
                            Json.decodeFromString<List<SavedFoodItem>>(p0.data.decodeToString())
                        cont.resume(foods) {
                            messenger.removeListener(this)
                        }
                    }
                }
            }

            messenger.addListener(listener)

            messenger.sendMessage(phoneNode!!, "/request/savedFood", "".encodeToByteArray())
                .addOnFailureListener { error ->
                    messenger.removeListener(listener)
                    if (cont.isActive) cont.resumeWithException(error)
                }

            cont.invokeOnCancellation {
                messenger.removeListener(listener)
            }

            cont.context.job.invokeOnCompletion { cause ->
                if (cause is TimeoutCancellationException && cont.isActive)
                    cont.resumeWithException(cause)
                messenger.removeListener(listener)
            }
        } else
            cont.resumeWithException(NullPointerException("No phone has been identified"))
    }

    fun start() {
        messenger.addListener(this)
    }

    fun sendMessage(path: String, data: ByteArray) {
        if (phoneNode != null)
            messenger.sendMessage(phoneNode!!, path, data)
    }

    suspend fun findPhone() {
        val nodes = nodeLister.connectedNodes.await()
        nodes.forEach { node ->
            Log.println(Log.INFO, "Message", "Requesting from ${node.displayName}")
            val data = "requestIdentity".encodeToByteArray()
            messenger.sendMessage(node.id, "/identify", data)
                .addOnFailureListener {
                    Log.i("Message", "Failed to send", it)
                }
                .addOnSuccessListener {
                    Log.i("Message", "Sent message to phone")
                }
        }
    }
}