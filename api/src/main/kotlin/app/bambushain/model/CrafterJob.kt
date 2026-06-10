package app.bambushain.model

import androidx.annotation.DrawableRes
import app.bambushain.api.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CrafterJob(val value: String) {

    @SerialName(value = "carpenter")
    Carpenter("carpenter"),

    @SerialName(value = "blacksmith")
    Blacksmith("blacksmith"),

    @SerialName(value = "armorer")
    Armorer("armorer"),

    @SerialName(value = "goldsmith")
    Goldsmith("goldsmith"),

    @SerialName(value = "leatherworker")
    Leatherworker("leatherworker"),

    @SerialName(value = "weaver")
    Weaver("weaver"),

    @SerialName(value = "alchemist")
    Alchemist("alchemist");

    /**
     * Override [toString()] to avoid using the enum variable name as the value, and instead use
     * the actual value defined in the API spec file.
     *
     * This solves a problem when the variable name and its value are different, and ensures that
     * the client sends the correct enum values to the server always.
     */
    override fun toString(): String = value

    @DrawableRes
    fun getIcon(): Int {
        return when (this) {
            Carpenter -> R.drawable.carpenter
            Blacksmith -> R.drawable.blacksmith
            Armorer -> R.drawable.armorer
            Goldsmith -> R.drawable.goldsmith
            Leatherworker -> R.drawable.leatherworker
            Weaver -> R.drawable.weaver
            Alchemist -> R.drawable.alchemist
        }
    }

    fun getDisplayName(): String {
        return when (this) {
            Carpenter -> "Zimmerer"
            Blacksmith -> "Grobschmied"
            Armorer -> "Plattner"
            Goldsmith -> "Goldschmied"
            Leatherworker -> "Gerber"
            Weaver -> "Weber"
            Alchemist -> "Alchemist"
        }
    }

    companion object {
        fun fromDisplayName(job: String): CrafterJob {
            return when (job) {
                "Zimmerer" -> Carpenter
                "Grobschmied" -> Blacksmith
                "Plattner" -> Armorer
                "Goldschmied" -> Goldsmith
                "Gerber" -> Leatherworker
                "Weber" -> Weaver
                "Alchemist" -> Alchemist
                else -> throw Exception()
            }
        }
    }

}

fun String.toCrafterJob(): CrafterJob {
    return CrafterJob.fromDisplayName(this)
}

