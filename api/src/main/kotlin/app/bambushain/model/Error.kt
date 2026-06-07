package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class Error(

    @SerialName(value = "entityType")
    val entityType: String? = null,

    @SerialName(value = "message")
    val message: String? = null,

    @SerialName(value = "errorType")
    val errorType: String? = null

) : Serializable, Parcelable