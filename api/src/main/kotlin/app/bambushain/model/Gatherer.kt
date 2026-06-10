package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class Gatherer(
    val job: GathererJob,
    val id: Int = -1,
    val level: String,
) : Parcelable