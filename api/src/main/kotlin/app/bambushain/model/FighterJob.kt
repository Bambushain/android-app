package app.bambushain.model

import androidx.annotation.DrawableRes
import app.bambushain.api.R
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

    @DrawableRes
    fun getIcon(): Int {
        return when (this) {
            Paladin -> R.drawable.paladin
            Warrior -> R.drawable.warrior
            DarkKnight -> R.drawable.darkknight
            Gunbreaker -> R.drawable.gunbreaker
            WhiteMage -> R.drawable.whitemage
            Scholar -> R.drawable.scholar
            Astrologian -> R.drawable.astrologian
            Sage -> R.drawable.sage
            Monk -> R.drawable.monk
            Dragoon -> R.drawable.dragoon
            Ninja -> R.drawable.ninja
            Samurai -> R.drawable.samurai
            Reaper -> R.drawable.reaper
            Bard -> R.drawable.bard
            Machinist -> R.drawable.machinist
            Dancer -> R.drawable.dancer
            BlackMage -> R.drawable.blackmage
            Summoner -> R.drawable.summoner
            RedMage -> R.drawable.redmage
            BlueMage -> R.drawable.bluemage
            Viper -> R.drawable.viper
            Pictomancer -> R.drawable.pictomancer
        }
    }

    fun getDisplayName(): String {
        return when (this) {
            Paladin -> "Paladin"
            Warrior -> "Krieger"
            DarkKnight -> "Dunkelritter"
            Gunbreaker -> "Revolverklinge"
            WhiteMage -> "Weißmagier"
            Scholar -> "Gelehrter"
            Astrologian -> "Astrologe"
            Sage -> "Weiser"
            Monk -> "Mönch"
            Dragoon -> "Dragoon"
            Ninja -> "Ninja"
            Samurai -> "Samurai"
            Reaper -> "Schnitter"
            Bard -> "Barde"
            Machinist -> "Maschinist"
            Dancer -> "Tänzer"
            BlackMage -> "Schwarzmagier"
            Summoner -> "Beschwörer"
            RedMage -> "Rotmagier"
            BlueMage -> "Blaumagier"
            Viper -> "Viper"
            Pictomancer -> "Piktomant"
        }
    }

    companion object {
        fun fromDisplayName(job: String): FighterJob {
            return when (job) {
                "Paladin" -> Paladin
                "Krieger" -> Warrior
                "Dunkelritter" -> DarkKnight
                "Revolverklinge" -> Gunbreaker
                "Weißmagier" -> WhiteMage
                "Gelehrter" -> Scholar
                "Astrologe" -> Astrologian
                "Weiser" -> Sage
                "Mönch" -> Monk
                "Dragoon" -> Dragoon
                "Ninja" -> Ninja
                "Samurai" -> Samurai
                "Schnitter" -> Reaper
                "Barde" -> Bard
                "Maschinist" -> Machinist
                "Tänzer" -> Dancer
                "Schwarzmagier" -> BlackMage
                "Beschwörer" -> Summoner
                "Rotmagier" -> RedMage
                "Blaumagier" -> BlueMage
                "Viper" -> Viper
                "Piktomant" -> Pictomancer
                else -> throw Exception()
            }
        }
    }
}

fun String.toFighterJob(): FighterJob {
    return FighterJob.fromDisplayName(this)
}
