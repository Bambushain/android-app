package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class Gatherer(

    @Contextual @SerialName(value = "job")
    val job: GathererJob,

    @SerialName(value = "id")
    val id: Int? = null,

    @SerialName(value = "level")
    val level: String? = null,

    @SerialName(value = "characterId")
    val characterId: Int? = null

) : Serializable, Parcelable