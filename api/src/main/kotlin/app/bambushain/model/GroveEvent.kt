package app.bambushain.model

import android.os.Parcelable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class GroveEvent(

    @SerialName(value = "title")
    val title: String,

    @SerialName(value = "description")
    val description: String,

    @Contextual @SerialName(value = "startDate")
    val startDate: LocalDate,

    @Contextual @SerialName(value = "endDate")
    val endDate: LocalDate,

    @Contextual @SerialName(value = "id")
    val id: Int? = null,

    @Contextual @SerialName(value = "startTime")
    val startTime: LocalTime? = null,

    val endTime: LocalTime? = null,

    val reminder: List<EventReminder>? = null,

    val color: String,
    val isPrivate: Boolean,
    val user: WebUser? = null,
    val grove: Grove? = null,
) : Serializable, Parcelable