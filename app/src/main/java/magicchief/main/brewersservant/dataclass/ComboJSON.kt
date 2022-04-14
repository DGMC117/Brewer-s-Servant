package magicchief.main.brewersservant.dataclass

import com.google.gson.annotations.SerializedName

data class ComboJSON (
        @SerializedName("Card 1")
        val card1: String?,
        @SerializedName("Card 2")
        val card2: String?,
        @SerializedName("Card 3")
        val card3: String?,
        @SerializedName("Card 4")
        val card4: String?,
        @SerializedName("Card 5")
        val card5: String?,
        @SerializedName("Card 6")
        val card6: String?,
        @SerializedName("Card 7")
        val card7: String?,
        @SerializedName("Card 8")
        val card8: String?,
        @SerializedName("Card 9")
        val card9: String?,
        @SerializedName("Card 10")
        val card10: String?,
        @SerializedName("Color Identity")
        val colorIdentity: String,
        @SerializedName("Prerequisites")
        val prerequisites: String,
        @SerializedName("Steps")
        val steps: String,
        @SerializedName("Results")
        val results: String
        )