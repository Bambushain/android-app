package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class UpdateProfile(

    @SerialName(value = "email")
    val email: String,

    @SerialName(value = "displayName")
    val displayName: String,

    @SerialName(value = "discordName")
    val discordName: String

) : Serializable, Parcelable