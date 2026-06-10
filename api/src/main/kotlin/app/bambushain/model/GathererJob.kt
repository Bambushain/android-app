package app.bambushain.model

import androidx.annotation.DrawableRes
import app.bambushain.api.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GathererJob(val value: String) {

    @SerialName(value = "culinarian")
    Culinarian("culinarian"),

    @SerialName(value = "miner")
    Miner("miner"),

    @SerialName(value = "botanist")
    Botanist("botanist"),

    @SerialName(value = "fisher")
    Fisher("fisher");

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
            Culinarian -> R.drawable.culinarian
            Miner -> R.drawable.miner
            Botanist -> R.drawable.botanist
            Fisher -> R.drawable.fisher
        }
    }

    fun getDisplayName(): String {
        return when (this) {
            Culinarian -> "Gourmet"
            Miner -> "Minenarbeiter"
            Botanist -> "Gärtner"
            Fisher -> "Fischer"
        }
    }

    companion object {
        fun fromDisplayName(job: String): GathererJob {
            return when (job) {
                "Gourmet" -> Culinarian
                "Minenarbeiter" -> Miner
                "Gärtner" -> Botanist
                "Fischer" -> Fisher
                else -> throw Exception()
            }
        }
    }

}

fun String.toGathererJob(): GathererJob {
    return GathererJob.fromDisplayName(this)
}
