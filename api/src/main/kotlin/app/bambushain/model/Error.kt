package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class Error(
    val entityType: String,
    val message: String,
    val errorType: String
) : Parcelable