package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class ResetPassword(

    @SerialName(value = "email")
    val email: String,

    @SerialName(value = "token")
    val token: String,

    @SerialName(value = "password")
    val password: String

) : Serializable, Parcelable