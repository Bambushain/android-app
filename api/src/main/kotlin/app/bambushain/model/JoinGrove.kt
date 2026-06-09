package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class JoinGrove(
    @Contextual
    val groveId: java.util.UUID
) : Parcelable