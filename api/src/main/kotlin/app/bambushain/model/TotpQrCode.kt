package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class TotpQrCode(

    @SerialName(value = "qrCode")
    val qrCode: String? = null,

    @SerialName(value = "secret")
    val secret: String? = null

) : Serializable, Parcelable