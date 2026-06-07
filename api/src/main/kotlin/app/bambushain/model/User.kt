package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class User(

    @SerialName(value = "id")
    val id: Int? = null,

    @SerialName(value = "email")
    val email: String? = null,

    @SerialName(value = "displayName")
    val displayName: String? = null,

    @SerialName(value = "discordName")
    val discordName: String? = null,

    @SerialName(value = "appTotpEnabled")
    val appTotpEnabled: Boolean? = null,

    @SerialName(value = "profilePicture")
    val profilePicture: String? = null

) : Serializable, Parcelable

@Parcelize
@KSerializable
data class WebUser(

    @SerialName(value = "id")
    val id: Int? = null,

    @SerialName(value = "email")
    val email: String? = null,

    @SerialName(value = "displayName")
    val displayName: String? = null,

    @SerialName(value = "discordName")
    val discordName: String? = null,

    ) : Serializable, Parcelable