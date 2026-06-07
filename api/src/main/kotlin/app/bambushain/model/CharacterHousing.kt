package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class CharacterHousing(

    @Contextual @SerialName(value = "district")
    val district: HousingDistrict,

    @Contextual @SerialName(value = "housingType")
    val housingType: HousingType,

    @SerialName(value = "ward")
    val ward: Int,

    @SerialName(value = "plot")
    val plot: Int,

    @SerialName(value = "id")
    val id: Int? = null,

    @SerialName(value = "characterId")
    val characterId: Int? = null

) : Serializable, Parcelable