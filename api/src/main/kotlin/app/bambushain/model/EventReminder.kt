package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class EventReminder(

    @Contextual @SerialName(value = "when")
    val `when`: kotlin.time.Instant,

    @SerialName(value = "id")
    val id: Int? = null

) : Serializable, Parcelable