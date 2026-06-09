package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class Fighter(
    val job: FighterJob,
    val id: Int = -1,
    val level: String,
    val gearScore: String,
) : Parcelable