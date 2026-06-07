package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class ChangePassword(

    @SerialName(value = "oldPassword")
    val oldPassword: String,

    @SerialName(value = "newPassword")
    val newPassword: String

) : Serializable, Parcelable