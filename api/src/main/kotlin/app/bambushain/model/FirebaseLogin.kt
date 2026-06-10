package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class FirebaseLogin(
    val token: String,
) : Parcelable
