package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class CustomFieldOption(
    val label: String,
    val id: Int = -1
) : Parcelable