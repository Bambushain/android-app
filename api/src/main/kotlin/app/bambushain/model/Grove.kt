package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class Grove(

    @Contextual @SerialName(value = "id")
    val id: Int? = null,

    @SerialName(value = "name")
    val name: String,

    @SerialName(value = "description")
    val description: String? = null,

    @SerialName(value = "inviteEnabled")
    val inviteEnabled: Boolean? = null,

    @Contextual @SerialName(value = "createdAt")
    val createdAt: kotlin.time.Instant? = null,

    @Contextual @SerialName(value = "updatedAt")
    val updatedAt: kotlin.time.Instant? = null

) : Serializable, Parcelable