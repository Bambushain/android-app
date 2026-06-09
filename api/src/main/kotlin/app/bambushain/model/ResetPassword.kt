package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class ResetPassword(
    val email: String,
    val token: String,
    val password: String
) : Parcelable