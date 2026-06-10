package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class EventReminder(
    val `when`: kotlin.time.Instant,
    val id: Int = -1
) : Parcelable