package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class Character(

    @Contextual @SerialName(value = "race")
    val race: CharacterRace,

    @SerialName(value = "name")
    val name: String,

    @SerialName(value = "world")
    val world: String,

    @SerialName(value = "id")
    val id: Int? = null,

    @SerialName(value = "datacenter")
    val datacenter: String? = null,

    @SerialName(value = "customFields")
    val customFields: List<CustomField>? = null,

    @SerialName(value = "freeCompany")
    val freeCompany: FreeCompany? = null

) : Serializable, Parcelable