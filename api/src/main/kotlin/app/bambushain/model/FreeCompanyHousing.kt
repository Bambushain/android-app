package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class FreeCompanyHousing(

    @Contextual @SerialName(value = "district")
    val district: HousingDistrict,

    @SerialName(value = "ward")
    val ward: Int,

    @SerialName(value = "plot")
    val plot: Int,

    @SerialName(value = "id")
    val id: Int? = null,

    @SerialName(value = "freeCompanyId")
    val freeCompanyId: Int? = null

) : Serializable, Parcelable