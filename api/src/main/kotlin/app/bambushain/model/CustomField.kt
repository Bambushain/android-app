package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class CustomField(
    val id: Int = -1,
    val label: String,
    val position: Int,
    val options: List<CustomFieldOption> = emptyList()
) : Parcelable