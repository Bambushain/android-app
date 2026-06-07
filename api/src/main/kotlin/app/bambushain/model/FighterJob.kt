package app.bambushain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 
 *
 * Values: paladin,warrior,darkKnight,gunbreaker,whiteMage,scholar,astrologian,sage,monk,dragoon,ninja,samurai,reaper,bard,machinist,dancer,blackMage,summoner,redMage,blueMage,viper,pictomancer
 */
@Serializable
enum class FighterJob(val value: String) {

    @SerialName(value = "paladin")
    Paladin("paladin"),

    @SerialName(value = "warrior")
    Warrior("warrior"),

    @SerialName(value = "dark-knight")
    DarkKnight("dark-knight"),

    @SerialName(value = "gunbreaker")
    Gunbreaker("gunbreaker"),

    @SerialName(value = "white-mage")
    WhiteMage("white-mage"),

    @SerialName(value = "scholar")
    Scholar("scholar"),

    @SerialName(value = "astrologian")
    Astrologian("astrologian"),

    @SerialName(value = "sage")
    Sage("sage"),

    @SerialName(value = "monk")
    Monk("monk"),

    @SerialName(value = "dragoon")
    Dragoon("dragoon"),

    @SerialName(value = "ninja")
    Ninja("ninja"),

    @SerialName(value = "samurai")
    Samurai("samurai"),

    @SerialName(value = "reaper")
    Reaper("reaper"),

    @SerialName(value = "bard")
    Bard("bard"),

    @SerialName(value = "machinist")
    Machinist("machinist"),

    @SerialName(value = "dancer")
    Dancer("dancer"),

    @SerialName(value = "black_mage")
    BlackMage("black_mage"),

    @SerialName(value = "summoner")
    Summoner("summoner"),

    @SerialName(value = "red-mage")
    RedMage("red-mage"),

    @SerialName(value = "blue-mage")
    BlueMage("blue-mage"),

    @SerialName(value = "viper")
    Viper("viper"),

    @SerialName(value = "pictomancer")
    Pictomancer("pictomancer");

    /**
     * Override [toString()] to avoid using the enum variable name as the value, and instead use
     * the actual value defined in the API spec file.
     *
     * This solves a problem when the variable name and its value are different, and ensures that
     * the client sends the correct enum values to the server always.
     */
    override fun toString(): String = value

}
