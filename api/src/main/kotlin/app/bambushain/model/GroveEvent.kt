package app.bambushain.model

import android.os.Parcelable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class GroveEvent(
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val id: Int = -1,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val reminder: List<EventReminder>? = null,
    val color: String,
    val isPrivate: Boolean,
    val user: WebUser? = null,
    val grove: Grove? = null,
) : Parcelable