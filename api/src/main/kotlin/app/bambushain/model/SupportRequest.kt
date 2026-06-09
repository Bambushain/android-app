package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class SupportRequest(
    val name: String,
    val email: String,
    val message: String
) : Parcelable