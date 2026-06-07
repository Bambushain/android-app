package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class CustomFieldOption(

    @SerialName(value = "label")
    val label: String,

    @SerialName(value = "id")
    val id: Int? = null

) : Serializable, Parcelable