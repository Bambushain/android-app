package app.bambushain.messaging

import android.app.Notification
import android.app.NotificationManager
import androidx.core.content.getSystemService
import app.bambushain.R
import app.bambushain.api.AuthenticationApi
import app.bambushain.model.FirebaseLogin
import app.bambushain.model.GroveEvent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import kotlinx.datetime.format
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject

@Serializable
data class EventReminder(val type: String, val payload: GroveEvent) {}

class MessagesService : FirebaseMessagingService() {

    private val authenticationApi: AuthenticationApi by inject()
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onMessageReceived(message: RemoteMessage) {
        val message = Json.decodeFromString<EventReminder>(message.data["payload"]!!)
        val `when` = if (message.payload.startTime != null) {
            message.payload.startDate.atTime(message.payload.startTime!!)
                .format(LocalDateTime.Format {
                    day()
                    chars(".")
                    monthNumber()
                    chars(".")
                    year()
                    chars(" um ")
                    hour()
                    chars(":")
                    minute()
                })
        } else {
            message.payload.startDate.format(LocalDate.Format {
                day()
                chars(".")
                monthNumber()
                chars(".")
                year()
            })
        }
        val notification = Notification
            .Builder(this, "event-reminder")
            .setContentTitle("Ereignis findet bald statt")
            .setContentText("Das Ereignis ${message.payload.title} findet am $`when` statt!")
            .setSmallIcon(R.drawable.ic_notification)
            .build()
        val notificationManager = getSystemService<NotificationManager>()
        notificationManager?.notify("event", message.payload.id, notification)
    }

    override fun onNewToken(token: String) {
        coroutineScope.launch {
            authenticationApi.firebaseLogin(FirebaseLogin(token))
        }
    }
}
