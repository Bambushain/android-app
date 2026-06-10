package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class CharacterCustomField(
    val label: String,
    val values: List<String>
) : Parcelable

@Parcelize
@KSerializable
data class Character(
    val race: CharacterRace,
    val name: String,
    val world: String,
    val id: Int = -1,
    val datacenter: String? = null,
    val customFields: List<CharacterCustomField>? = null,
    val freeCompany: FreeCompany? = null

) : Parcelable
