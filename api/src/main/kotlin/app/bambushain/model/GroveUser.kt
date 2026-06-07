package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class GroveUser(

    @SerialName(value = "id")
    val id: Int? = null,

    @SerialName(value = "email")
    val email: String? = null,

    @SerialName(value = "displayName")
    val displayName: String? = null,

    @SerialName(value = "discordName")
    val discordName: String? = null,

    @SerialName(value = "isMod")
    val isMod: Boolean? = null,

    @SerialName(value = "isBanned")
    val isBanned: Boolean? = null

) : Serializable, Parcelable