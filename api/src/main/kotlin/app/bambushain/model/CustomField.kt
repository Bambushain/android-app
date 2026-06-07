package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable

data class CustomField(

    @SerialName(value = "id")
    val id: Int? = null,

    @SerialName(value = "label")
    val label: String? = null,

    @SerialName(value = "position")
    val position: Int? = null,

    @SerialName(value = "options")
    val options: List<CustomFieldOption>? = null

) : Serializable, Parcelable