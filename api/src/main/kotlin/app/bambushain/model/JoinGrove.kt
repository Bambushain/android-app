package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class JoinGrove(

    @Contextual @SerialName(value = "groveId")
    val groveId: java.util.UUID

) : Serializable, Parcelable