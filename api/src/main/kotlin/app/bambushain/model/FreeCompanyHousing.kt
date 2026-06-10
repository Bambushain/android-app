package app.bambushain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable as KSerializable

@Parcelize
@KSerializable
data class FreeCompanyHousing(
    val district: HousingDistrict,
    val ward: Int,
    val plot: Int,
    val id: Int = -1,
) : Parcelable