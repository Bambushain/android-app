package app.bambushain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class HousingDistrict(val value: String) {

    @SerialName(value = "the-lavender-beds")
    TheLavenderBeds("the-lavender-beds"),

    @SerialName(value = "mist")
    Mist("mist"),

    @SerialName(value = "the-goblet")
    TheGoblet("the-goblet"),

    @SerialName(value = "shirogane")
    Shirogane("shirogane"),

    @SerialName(value = "empyreum")
    Empyreum("empyreum");

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
            TheLavenderBeds -> "Lavendelbeete"
            Mist -> "Dorf des Nebels"
            TheGoblet -> "Kelchkuppe"
            Shirogane -> "Shirogane"
            Empyreum -> "Empyreum"
        }
    }

    companion object {
        fun fromDisplayName(district: String): HousingDistrict {
            return when (district) {
                "Lavendelbeete" -> TheLavenderBeds
                "Dorf des Nebels" -> Mist
                "Kelchkuppe" -> TheGoblet
                "Shirogane" -> Shirogane
                "Empyreum" -> Empyreum
                else -> throw Exception()
            }
        }
    }
}

fun String.toHousingDistrict(): HousingDistrict {
    return HousingDistrict.fromDisplayName(this)
}
