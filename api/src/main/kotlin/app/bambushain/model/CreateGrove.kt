package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class CreateGrove(

    @SerialName(value = "name")
    val name: String,

    @SerialName(value = "description")
    val description: String? = null

) : Serializable, Parcelable
