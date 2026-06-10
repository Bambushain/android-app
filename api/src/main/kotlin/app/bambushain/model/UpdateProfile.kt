package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class UpdateProfile(
    val email: String,
    val displayName: String,
    val discordName: String
) : Parcelable