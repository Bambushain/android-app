package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class Login(
    val email: String,
    val password: String,
    val twoFactorCode: String? = null
) : Parcelable