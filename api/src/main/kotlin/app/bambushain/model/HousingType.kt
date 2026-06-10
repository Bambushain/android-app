package app.bambushain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class HousingType(val value: String) {

    @SerialName(value = "private")
    Private("private"),

    @SerialName(value = "shared-apartment")
    SharedApartment("shared-apartment");

    /**
     * Override [toString()] to avoid using the enum variable name as the value, and instead use
     * the actual value defined in the API spec file.
     *
     * This solves a problem when the variable name and its value are different, and ensures that
     * the client sends the correct enum values to the server always.
     */
    override fun toString(): String = value

    fun getDisplayName(): String {
        return when (this) {
            Private -> "Private Unterkunft"
            SharedApartment -> "Wohngemeinschaft"
        }
    }

    companion object {
        fun fromDisplayName(type: String): HousingType {
            return when (type) {
                "Private Unterkunft" -> Private
                "Wohngemeinschaft" -> SharedApartment
                else -> throw Exception()
            }
        }
    }
}

fun String.toHousingType(): HousingType {
    return HousingType.fromDisplayName(this)
}
