package app.bambushain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CharacterRace(val value: String) {

    @SerialName(value = "hyur")
    Hyur("hyur"),

    @SerialName(value = "elezen")
    Elezen("elezen"),

    @SerialName(value = "lalafell")
    Lalafell("lalafell"),

    @SerialName(value = "miqote")
    Miqote("miqote"),

    @SerialName(value = "roegadyn")
    Roegadyn("roegadyn"),

    @SerialName(value = "au-ra")
    AuRa("au-ra"),

    @SerialName(value = "hrothgar")
    Hrothgar("hrothgar"),

    @SerialName(value = "viera")
    Viera("viera");

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
            Hyur -> "Hyuran"
            Elezen -> "Elezen"
            Lalafell -> "Lalafell"
            Miqote -> "Miqo'te"
            Roegadyn -> "Roegadyn"
            AuRa -> "Au Ra"
            Hrothgar -> "Hrothgar"
            Viera -> "Viera"
        }
    }

    companion object {
        fun fromDisplayName(race: String): CharacterRace {
            return when (race) {
                "Hyuran" -> Hyur
                "Elezen" -> Elezen
                "Lalafell" -> Lalafell
                "Miqo'te" -> Miqote
                "Roegadyn" -> Roegadyn
                "Au Ra" -> AuRa
                "Hrothgar" -> Hrothgar
                "Viera" -> Viera
                else -> throw Exception()
            }
        }
    }
}

fun String.toCharacterRace(): CharacterRace {
    return CharacterRace.fromDisplayName(this)
}
