package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class ValidateTotp(

    @SerialName(value = "code")
    val code: String,

    @SerialName(value = "password")
    val password: String

) : Serializable, Parcelable