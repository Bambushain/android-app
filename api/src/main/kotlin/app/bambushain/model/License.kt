package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class License(

    @SerialName(value = "name")
    val name: String? = null,

    @SerialName(value = "version")
    val version: String? = null,

    @SerialName(value = "license")
    val license: String? = null

) : Serializable, Parcelable