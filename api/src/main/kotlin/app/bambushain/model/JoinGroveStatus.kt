package app.bambushain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class JoinGroveStatus(val value: String) {

    @SerialName(value = "joined")
    Joined("joined"),

    @SerialName(value = "not-joined")
    NotJoined("not-joined"),

    @SerialName(value = "banned")
    Banned("banned");

    /**
     * Override [toString()] to avoid using the enum variable name as the value, and instead use
     * the actual value defined in the API spec file.
     *
     * This solves a problem when the variable name and its value are different, and ensures that
     * the client sends the correct enum values to the server always.
     */
    override fun toString(): String = value

}
