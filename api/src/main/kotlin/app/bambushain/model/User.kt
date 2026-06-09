package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class User(
    val id: Int,
    val email: String,
    val displayName: String,
    val discordName: String,
) : Parcelable

@Parcelize
@KSerializable
data class WebUser(
    val id: Int,
    val email: String,
    val displayName: String,
    val discordName: String,
) : Parcelable
