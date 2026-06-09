package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class TotpQrCode(
    val qrCode: String? = null,
    val secret: String? = null
) : Parcelable