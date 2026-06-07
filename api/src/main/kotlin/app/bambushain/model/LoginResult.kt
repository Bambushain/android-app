package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class LoginResult(

    @SerialName(value = "user")
    val user: User? = null,

    @SerialName(value = "token")
    val token: String? = null

) : Serializable, Parcelable