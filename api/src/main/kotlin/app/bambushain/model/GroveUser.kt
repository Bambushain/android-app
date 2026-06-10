package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class GroveUser(
    val id: Int = -1,
    val email: String,
    val displayName: String,
    val discordName: String,
    val isMod: Boolean,
    val isBanned: Boolean
) : Parcelable