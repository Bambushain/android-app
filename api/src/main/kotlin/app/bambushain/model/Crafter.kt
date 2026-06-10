package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class Crafter(
    val job: CrafterJob,
    val level: String,
    val id: Int = -1,
) : Parcelable

