package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class Login(

    @SerialName(value = "email")
    val email: String,

    @SerialName(value = "password")
    val password: String,

    @SerialName(value = "twoFactorCode")
    val twoFactorCode: String? = null

) : Serializable, Parcelable