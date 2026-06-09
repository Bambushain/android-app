package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class Grove(
    val id: Int = -1,
    val name: String,
    val description: String? = null,
    val inviteEnabled: Boolean? = null,
    val createdAt: kotlin.time.Instant? = null,
    val updatedAt: kotlin.time.Instant? = null
) : Parcelable