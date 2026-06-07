package app.bambushain.model

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

}
