package magicchief.main.brewersservant

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import magicchief.main.brewersservant.dataclass.*
import magicchief.main.brewersservant.dataclass.Set
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList

class DBHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    val parentContext = context

    override fun onCreate(db: SQLiteDatabase) {
        // Define SQL instructions
        // Create Card table
        val createCardDB = (
                "CREATE TABLE $CARD_TABLE_NAME ("
                        + CARD_ID + " INTEGER primary key autoincrement,"
                        + CARD_SCRYFALL_ID + " char(36) unique not null,"
                        + CARD_CMC + " float not null,"
                        + CARD_COLOR_IDENTITY + " varchar(50) not null,"
                        + CARD_COLORS + " varchar(50),"
                        + CARD_LAYOUT + " varchar(50) not null,"
                        + CARD_LOYALTY + " varchar(25),"
                        + CARD_MANA_COST + " varchar(100),"
                        + CARD_NAME + " varchar(255) not null,"
                        + CARD_ORACLE_TEXT + " text,"
                        + CARD_POWER + " varchar(25),"
                        + CARD_PRODUCED_MANA + " varchar(100),"
                        + CARD_TOUGHNESS + " varchar(25),"
                        + CARD_TYPE_LINE + " varchar(255) not null,"
                        + CARD_ARTIST + " varchar(50),"
                        + CARD_FLAVOR_TEXT + " text,"
                        + CARD_RARITY + " varchar(50) not null,"
                        + CARD_SET_SCRYFALL_ID + " char(36) not null,"
                        + CARD_PRICE_USD + " double,"
                        + CARD_PRICE_EUR + " double,"
                        + CARD_PRICE_TIX + " double,"
                        + CARD_LEGAL_STANDARD + " varchar(25) not null,"
                        + CARD_LEGAL_PIONEER + " varchar(25) not null,"
                        + CARD_LEGAL_MODERN + " varchar(25) not null,"
                        + CARD_LEGAL_LEGACY + " varchar(25) not null,"
                        + CARD_LEGAL_VINTAGE + " varchar(25) not null,"
                        + CARD_LEGAL_BRAWL + " varchar(25) not null,"
                        + CARD_LEGAL_HISTORIC + " varchar(25) not null,"
                        + CARD_LEGAL_PAUPER + " varchar(25) not null,"
                        + CARD_LEGAL_PENNY + " varchar(25) not null,"
                        + CARD_LEGAL_COMMANDER + " varchar(25) not null,"
                        + CARD_IMAGE_PNG + " varchar(255),"
                        + CARD_IMAGE_BORDER_CROP + " varchar(255),"
                        + CARD_IMAGE_ART_CROP + " varchar(255),"
                        + CARD_IMAGE_LARGE + " varchar(255),"
                        + CARD_IMAGE_NORMAL + " varchar(255),"
                        + CARD_IMAGE_SMALL + " varchar(255),"
                        + "foreign key ($CARD_SET_SCRYFALL_ID) references $CARD_SET_TABLE_NAME($CARD_SET_SCRYFALL_SET_ID)"
                        + ")"
                )
        // Create CardFace table
        val createCardFaceDB = (
                "CREATE TABLE $CARD_FACE_TABLE_NAME ("
                        + CARD_FACE_ID + " INTEGER primary key autoincrement,"
                        + CARD_FACE_SCRYFALL_ID_MAIN_CARD + " char(36) not null,"
                        + CARD_FACE_CMC + " float,"
                        + CARD_FACE_COLORS + " varchar(50),"
                        + CARD_FACE_LAYOUT + " varchar(50),"
                        + CARD_FACE_LOYALTY + " varchar(25),"
                        + CARD_FACE_MANA_COST + " varchar(100) not null,"
                        + CARD_FACE_NAME + " varchar(255) not null,"
                        + CARD_FACE_ORACLE_TEXT + " text,"
                        + CARD_FACE_POWER + " varchar(25),"
                        + CARD_FACE_TOUGHNESS + " varchar(25),"
                        + CARD_FACE_TYPE_LINE + " varchar(255),"
                        + CARD_FACE_ARTIST + " varchar(50),"
                        + CARD_FACE_FLAVOR_TEXT + " text,"
                        + CARD_FACE_IMAGE_PNG + " varchar(255),"
                        + CARD_FACE_IMAGE_BORDER_CROP + " varchar(255),"
                        + CARD_FACE_IMAGE_ART_CROP + " varchar(255),"
                        + CARD_FACE_IMAGE_LARGE + " varchar(255),"
                        + CARD_FACE_IMAGE_NORMAL + " varchar(255),"
                        + CARD_FACE_IMAGE_SMALL + " varchar(255),"
                        + "foreign key ($CARD_FACE_SCRYFALL_ID_MAIN_CARD) references $CARD_TABLE_NAME($CARD_SCRYFALL_ID)"
                        + ")"
                )
        // Create RelatedCard table
        val createRelatedCardDB = (
                "CREATE TABLE $RELATED_CARD_TABLE_NAME ("
                        + RELATED_CARD_ID + " INTEGER primary key autoincrement,"
                        + RELATED_CARD_SCRYFALL_ID_MAIN + " char(36) not null,"
                        + RELATED_CARD_SCRYFALL_ID_RELATED + " char(36) not null,"
                        + RELATED_CARD_COMPONENT + " varchar(25) not null,"
                        + RELATED_CARD_NAME + " varchar(255) not null,"
                        + "foreign key ($RELATED_CARD_SCRYFALL_ID_MAIN) references $CARD_TABLE_NAME($CARD_SCRYFALL_ID),"
                        + "foreign key ($RELATED_CARD_SCRYFALL_ID_RELATED) references $CARD_TABLE_NAME($CARD_SCRYFALL_ID)"
                        + ")"
                )
        // Create CardSet table
        val createCardSetDB = (
                "CREATE TABLE $CARD_SET_TABLE_NAME ("
                        + CARD_SET_SET_ID + " INTEGER primary key autoincrement,"
                        + CARD_SET_SCRYFALL_SET_ID + " char(36) not null,"
                        + CARD_SET_CODE + " varchar(10) not null,"
                        + CARD_SET_NAME + " varchar(50) not null,"
                        + CARD_SET_ICON_SVG_URI + " varchar(255) not null"
                        + ")"
                )
        // Create Catalog tables
        val createArtistCatalogDB = (
                "CREATE TABLE $ARTIST_CATALOG_TABLE_NAME ("
                        + ARTIST_CATALOG_ID + " INTEGER primary key autoincrement,"
                        + ARTIST_CATALOG_NAME + " varchar(50) not null"
                        + ")"
                )
        val createCreatureTypeCatalogDB = (
                "CREATE TABLE $CREATURE_TYPE_CATALOG_TABLE_NAME ("
                        + CREATURE_TYPE_CATALOG_ID + " INTEGER primary key autoincrement,"
                        + CREATURE_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createPlaneswalkerTypeCatalogDB = (
                "CREATE TABLE $PLANESWALKER_TYPE_CATALOG_TABLE_NAME ("
                        + PLANESWALKER_TYPE_CATALOG_ID + " INTEGER primary key autoincrement,"
                        + PLANESWALKER_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createLandTypeCatalogDB = (
                "CREATE TABLE $LAND_TYPE_CATALOG_TABLE_NAME ("
                        + LAND_TYPE_CATALOG_ID + " INTEGER primary key autoincrement,"
                        + LAND_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createArtifactTypeCatalogDB = (
                "CREATE TABLE $ARTIFACT_TYPE_CATALOG_TABLE_NAME ("
                        + ARTIFACT_TYPE_CATALOG_ID + " INTEGER primary key autoincrement,"
                        + ARTIFACT_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createEnchantmentTypeCatalogDB = (
                "CREATE TABLE $ENCHANTMENT_TYPE_CATALOG_TABLE_NAME ("
                        + ENCHANTMENT_TYPE_CATALOG_ID + " INTEGER primary key autoincrement,"
                        + ENCHANTMENT_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createSpellTypeCatalogDB = (
                "CREATE TABLE $SPELL_TYPE_CATALOG_TABLE_NAME ("
                        + SPELL_TYPE_CATALOG_ID + " INTEGER primary key autoincrement,"
                        + SPELL_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createCardTypeCatalogDB = (
                "CREATE TABLE $CARD_TYPE_CATALOG_TABLE_NAME ("
                        + CARD_TYPE_CATALOG_ID + " INTEGER primary key autoincrement,"
                        + CARD_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createCardSuperTypeCatalogDB = (
                "CREATE TABLE $CARD_SUPER_TYPE_CATALOG_TABLE_NAME ("
                        + CARD_SUPER_TYPE_CATALOG_ID + " INTEGER primary key autoincrement,"
                        + CARD_SUPER_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createCombosDB = (
                "CREATE TABLE $COMBO_TABLE_NAME ("
                        + COMBO_ID + " INTEGER primary key,"
                        + COMBO_COLOR_IDENTITY + " varchar(50) not null,"
                        + COMBO_PREREQUISITES + " text not null,"
                        + COMBO_STEPS + " text not null,"
                        + COMBO_RESULTS + " text not null"
                        + ")"
                )
        val createCardsInCombosDB = (
                "CREATE TABLE $CIC_TABLE_NAME ("
                        + CIC_COMBO_ID + " INTEGER,"
                        + CIC_CARD_NAME + " varchar(255),"
                        + "primary key ($CIC_COMBO_ID, $CIC_CARD_NAME),"
                        + "foreign key ($CIC_COMBO_ID) references $COMBO_TABLE_NAME($COMBO_ID),"
                        + "foreign key ($CIC_CARD_NAME) references $CARD_TABLE_NAME($CARD_NAME)"
                        + ")"
                )
        val createDecksDB = (
                "CREATE TABLE $DECK_TABLE_NAME ("
                        + DECK_ID + " INTEGER primary key autoincrement,"
                        + DECK_NAME + " varchar(255) not null,"
                        + DECK_FORMAT + " varchar(50) not null,"
                        + DECK_FACE_CARD_URI + " varchar(255),"
                        + DECK_COLOR_IDENTITY + " varchar(50) not null"
                        + ")"
                )
        val createCardsInDecksDB = (
                "CREATE TABLE $CID_TABLE_NAME ("
                        + CID_DECK_ID + " INTEGER,"
                        + CID_CARD_ID + " char(36),"
                        + CID_AMOUNT + " int,"
                        + CID_BOARD + " varchar(50),"
                        + "primary key ($CID_DECK_ID, $CID_CARD_ID),"
                        + "foreign key ($CID_DECK_ID) references $DECK_TABLE_NAME($DECK_ID),"
                        + "foreign key ($CID_CARD_ID) references $CARD_TABLE_NAME($CARD_SCRYFALL_ID)"
                        + ")"
                )

        // Execute SQL code
        db.execSQL(createCardSetDB)
        db.execSQL(createCardDB)
        db.execSQL(createCardFaceDB)
        db.execSQL(createRelatedCardDB)
        db.execSQL(createArtistCatalogDB)
        db.execSQL(createCreatureTypeCatalogDB)
        db.execSQL(createPlaneswalkerTypeCatalogDB)
        db.execSQL(createLandTypeCatalogDB)
        db.execSQL(createArtifactTypeCatalogDB)
        db.execSQL(createEnchantmentTypeCatalogDB)
        db.execSQL(createSpellTypeCatalogDB)
        db.execSQL(createCardTypeCatalogDB)
        db.execSQL(createCardSuperTypeCatalogDB)
        db.execSQL(createCombosDB)
        db.execSQL(createCardsInCombosDB)
        db.execSQL(createDecksDB)
        db.execSQL(createCardsInDecksDB)

        initialiseCardTypesAndSuperTypes (db)

    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // This method drops the tables if they existed and creates them again
        db.execSQL("DROP TABLE IF EXISTS $CIC_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $COMBO_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $CARD_SUPER_TYPE_CATALOG_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $CARD_TYPE_CATALOG_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $SPELL_TYPE_CATALOG_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $ENCHANTMENT_TYPE_CATALOG_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $ARTIFACT_TYPE_CATALOG_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $LAND_TYPE_CATALOG_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $PLANESWALKER_TYPE_CATALOG_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $CREATURE_TYPE_CATALOG_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $ARTIST_CATALOG_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $CARD_FACE_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $RELATED_CARD_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $CARD_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $CARD_SET_TABLE_NAME")

        onCreate(db)
    }

    companion object {
        // Database variables

        // Database name
        private val DATABASE_NAME = "PaintersServantDB"

        // Database version
        private val DATABASE_VERSION = 1

        // Card table name
        private val CARD_TABLE_NAME = "Card"

        // Card table column names
        private val CARD_ID = "card_id"
        private val CARD_SCRYFALL_ID = "scryfall_id"
        private val CARD_CMC = "cmc"
        private val CARD_COLOR_IDENTITY = "color_identity"
        private val CARD_COLORS = "colors"
        private val CARD_LAYOUT = "layout"
        private val CARD_LOYALTY = "loyalty"
        private val CARD_MANA_COST = "mana_cost"
        private val CARD_NAME = "name"
        private val CARD_ORACLE_TEXT = "oracle_text"
        private val CARD_POWER = "power"
        private val CARD_PRODUCED_MANA = "produced_mana"
        private val CARD_TOUGHNESS = "toughness"
        private val CARD_TYPE_LINE = "type_line"
        private val CARD_ARTIST = "artist"
        private val CARD_FLAVOR_TEXT = "flavor_text"
        private val CARD_RARITY = "rarity"
        private val CARD_SET_SCRYFALL_ID = "scryfall_set_id"
        private val CARD_PRICE_USD = "price_usd"
        private val CARD_PRICE_EUR = "price_eur"
        private val CARD_PRICE_TIX = "price_tix"
        private val CARD_LEGAL_STANDARD = "legal_standard"
        private val CARD_LEGAL_PIONEER = "legal_pioneer"
        private val CARD_LEGAL_MODERN = "legal_modern"
        private val CARD_LEGAL_LEGACY = "legal_legacy"
        private val CARD_LEGAL_VINTAGE = "legal_vintage"
        private val CARD_LEGAL_BRAWL = "legal_brawl"
        private val CARD_LEGAL_HISTORIC = "legal_historic"
        private val CARD_LEGAL_PAUPER = "legal_pauper"
        private val CARD_LEGAL_PENNY = "legal_penny"
        private val CARD_LEGAL_COMMANDER = "legal_commander"
        private val CARD_IMAGE_PNG = "image_png"
        private val CARD_IMAGE_BORDER_CROP = "image_border_crop"
        private val CARD_IMAGE_ART_CROP = "image_art_crop"
        private val CARD_IMAGE_LARGE = "image_large"
        private val CARD_IMAGE_NORMAL = "image_normal"
        private val CARD_IMAGE_SMALL = "image_small"

        // CardFace table name
        private val CARD_FACE_TABLE_NAME = "CardFace"

        // CardFace table column names
        private val CARD_FACE_ID = "card_face_id"
        private val CARD_FACE_SCRYFALL_ID_MAIN_CARD = "card_face_id_main_card_scryfall"
        private val CARD_FACE_ARTIST = "card_face_artist"
        private val CARD_FACE_CMC = "card_face_cmc"
        private val CARD_FACE_COLORS = "card_face_colors"
        private val CARD_FACE_FLAVOR_TEXT = "card_face_flavor_text"
        private val CARD_FACE_LAYOUT = "card_face_layout"
        private val CARD_FACE_LOYALTY = "card_face_loyalty"
        private val CARD_FACE_MANA_COST = "card_face_mana_cost"
        private val CARD_FACE_NAME = "card_face_name"
        private val CARD_FACE_ORACLE_TEXT = "card_face_oracle_text"
        private val CARD_FACE_POWER = "card_face_power"
        private val CARD_FACE_TOUGHNESS = "card_face_toughness"
        private val CARD_FACE_TYPE_LINE = "card_face_type_line"
        private val CARD_FACE_IMAGE_PNG = "card_face_image_png"
        private val CARD_FACE_IMAGE_BORDER_CROP = "card_face_image_border_crop"
        private val CARD_FACE_IMAGE_ART_CROP = "card_face_image_art_crop"
        private val CARD_FACE_IMAGE_LARGE = "card_face_image_large"
        private val CARD_FACE_IMAGE_NORMAL = "card_face_image_normal"
        private val CARD_FACE_IMAGE_SMALL = "card_face_image_small"

        // RelatedCard table name
        private val RELATED_CARD_TABLE_NAME = "RelatedCard"

        // RelatedCard table column names
        private val RELATED_CARD_ID = "related_card_id"
        private val RELATED_CARD_SCRYFALL_ID_MAIN = "related_card_id_main_scryfall"
        private val RELATED_CARD_SCRYFALL_ID_RELATED = "related_card_id_related_scryfall"
        private val RELATED_CARD_COMPONENT = "related_card_component"
        private val RELATED_CARD_NAME = "related_card_name"

        // CardSet table name
        private val CARD_SET_TABLE_NAME = "CardSet"

        // CardSet table column names
        private val CARD_SET_SET_ID = "set_id"
        private val CARD_SET_SCRYFALL_SET_ID = "scryfall_set_id"
        private val CARD_SET_CODE = "code"
        private val CARD_SET_NAME = "name"
        private val CARD_SET_ICON_SVG_URI = "icon_svg_uri"

        // Catalog Tables
        private val ARTIST_CATALOG_TABLE_NAME = "ArtistCatalog"
        private val ARTIST_CATALOG_ID = "artist_id"
        private val ARTIST_CATALOG_NAME = "name"

        private val CREATURE_TYPE_CATALOG_TABLE_NAME = "CreatureTypeCatalog"
        private val CREATURE_TYPE_CATALOG_ID = "creature_id"
        private val CREATURE_TYPE_CATALOG_TYPE = "type"

        private val PLANESWALKER_TYPE_CATALOG_TABLE_NAME = "PlaneswalkerTypeCatalog"
        private val PLANESWALKER_TYPE_CATALOG_ID = "planeswalker_id"
        private val PLANESWALKER_TYPE_CATALOG_TYPE = "type"

        private val LAND_TYPE_CATALOG_TABLE_NAME = "LandTypeCatalog"
        private val LAND_TYPE_CATALOG_ID = "land_id"
        private val LAND_TYPE_CATALOG_TYPE = "type"

        private val ARTIFACT_TYPE_CATALOG_TABLE_NAME = "ArtifactTypeCatalog"
        private val ARTIFACT_TYPE_CATALOG_ID = "artifact_id"
        private val ARTIFACT_TYPE_CATALOG_TYPE = "type"

        private val ENCHANTMENT_TYPE_CATALOG_TABLE_NAME = "EnchantmentTypeCatalog"
        private val ENCHANTMENT_TYPE_CATALOG_ID = "enchantment_id"
        private val ENCHANTMENT_TYPE_CATALOG_TYPE = "type"

        private val SPELL_TYPE_CATALOG_TABLE_NAME = "SpellTypeCatalog"
        private val SPELL_TYPE_CATALOG_ID = "spell_id"
        private val SPELL_TYPE_CATALOG_TYPE = "type"

        private val CARD_TYPE_CATALOG_TABLE_NAME = "CardTypeCatalog"
        private val CARD_TYPE_CATALOG_ID = "cardtype_id"
        private val CARD_TYPE_CATALOG_TYPE = "type"

        private val CARD_SUPER_TYPE_CATALOG_TABLE_NAME = "CardSuperTypeCatalog"
        private val CARD_SUPER_TYPE_CATALOG_ID = "supertype_id"
        private val CARD_SUPER_TYPE_CATALOG_TYPE = "type"

        // Card Combos
        private val COMBO_TABLE_NAME = "Combo"

        private val COMBO_ID = "combo_id"
        private val COMBO_COLOR_IDENTITY = "combo_color_identity"
        private val COMBO_PREREQUISITES = "combo_prerequisites"
        private val COMBO_STEPS = "combo_steps"
        private val COMBO_RESULTS = "combo_results"

        private val CIC_TABLE_NAME = "CardsInCombos"

        private val CIC_CARD_NAME = "cic_card_name"
        private val CIC_COMBO_ID = "cic_combo_id"

        // Card Decks
        private val DECK_TABLE_NAME = "Deck"

        private val DECK_ID = "deck_id"
        private val DECK_NAME = "deck_name"
        private val DECK_FORMAT = "deck_format"
        private val DECK_FACE_CARD_URI = "deck_face_card"
        private val DECK_COLOR_IDENTITY = "deck_color_identity"

        private val CID_TABLE_NAME = "CardsInDecks"

        private val CID_CARD_ID = "cid_card_id"
        private val CID_DECK_ID = "cid_deck_id"
        private val CID_AMOUNT = "cid_amount"
        private val CID_BOARD = "cid_board"
    }

    fun addCardSet(cardSet: Set): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CARD_SET_SCRYFALL_SET_ID, cardSet.id.toString())
        contentValues.put(CARD_SET_CODE, cardSet.code)
        contentValues.put(CARD_SET_NAME, cardSet.name)
        contentValues.put(CARD_SET_ICON_SVG_URI, cardSet.icon_svg_uri.toString())
        val result = db.insert(CARD_SET_TABLE_NAME, null, contentValues)
        return result
    }

    fun addCard(card: Card): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CARD_SCRYFALL_ID, card.id.toString())
        contentValues.put(CARD_CMC, card.cmc)
        contentValues.put(CARD_COLOR_IDENTITY, colorArrayToString(card.color_identity))
        contentValues.put(CARD_COLORS, colorArrayToString(card.colors)) // Missing Null Check
        contentValues.put(CARD_LAYOUT, card.layout)
        contentValues.put(CARD_LOYALTY, card.loyalty) // Missing Null Check
        contentValues.put(CARD_MANA_COST, card.mana_cost) // Missing Null Check
        contentValues.put(CARD_NAME, card.name)
        contentValues.put(CARD_ORACLE_TEXT, card.oracle_text) // Missing Null Check
        contentValues.put(CARD_POWER, card.power) // Missing Null Check
        contentValues.put(
            CARD_PRODUCED_MANA,
            colorArrayToString(card.produced_mana)
        ) // Missing Null Check
        contentValues.put(CARD_TOUGHNESS, card.toughness) // Missing Null Check
        contentValues.put(CARD_TYPE_LINE, card.type_line)
        contentValues.put(CARD_ARTIST, card.artist) // Missing Null Check
        contentValues.put(CARD_FLAVOR_TEXT, card.flavor_text) // Missing Null Check
        contentValues.put(CARD_RARITY, card.rarity)
        contentValues.put(CARD_SET_SCRYFALL_ID, card.set_id)
        contentValues.put(CARD_PRICE_USD, card.prices?.usd?.toDouble())
        contentValues.put(CARD_PRICE_EUR, card.prices?.eur?.toDouble())
        contentValues.put(CARD_PRICE_TIX, card.prices?.tix?.toDouble())
        contentValues.put(CARD_LEGAL_STANDARD, card.legalities?.standard)
        contentValues.put(CARD_LEGAL_PIONEER, card.legalities?.pioneer)
        contentValues.put(CARD_LEGAL_MODERN, card.legalities?.modern)
        contentValues.put(CARD_LEGAL_LEGACY, card.legalities?.legacy)
        contentValues.put(CARD_LEGAL_VINTAGE, card.legalities?.vintage)
        contentValues.put(CARD_LEGAL_BRAWL, card.legalities?.brawl)
        contentValues.put(CARD_LEGAL_HISTORIC, card.legalities?.historic)
        contentValues.put(CARD_LEGAL_PAUPER, card.legalities?.pauper)
        contentValues.put(CARD_LEGAL_PENNY, card.legalities?.penny)
        contentValues.put(CARD_LEGAL_COMMANDER, card.legalities?.commander)
        contentValues.put(
            CARD_IMAGE_PNG,
            if (card.image_uris != null) card.image_uris.png.toString() else null
        )
        contentValues.put(
            CARD_IMAGE_BORDER_CROP,
            if (card.image_uris != null) card.image_uris.border_crop.toString() else null
        )
        contentValues.put(
            CARD_IMAGE_ART_CROP,
            if (card.image_uris != null) card.image_uris.art_crop.toString() else null
        )
        contentValues.put(
            CARD_IMAGE_LARGE,
            if (card.image_uris != null) card.image_uris.large.toString() else null
        )
        contentValues.put(
            CARD_IMAGE_NORMAL,
            if (card.image_uris != null) card.image_uris.normal.toString() else null
        )
        contentValues.put(
            CARD_IMAGE_SMALL,
            if (card.image_uris != null) card.image_uris.small.toString() else null
        )
        val result = db.insert(CARD_TABLE_NAME, null, contentValues)
        return result
    }

    fun addCardFace(cardFace: CardFace, mainCardId: UUID): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CARD_FACE_SCRYFALL_ID_MAIN_CARD, mainCardId.toString())
        contentValues.put(CARD_FACE_CMC, cardFace.cmc) // Missing Null Check
        contentValues.put(
            CARD_FACE_COLORS,
            colorArrayToString(cardFace.colors)
        ) // Missing Null Check
        contentValues.put(CARD_FACE_LAYOUT, cardFace.layout) // Missing Null Check
        contentValues.put(CARD_FACE_LOYALTY, cardFace.loyalty) // Missing Null Check
        contentValues.put(CARD_FACE_MANA_COST, cardFace.mana_cost)
        contentValues.put(CARD_FACE_NAME, cardFace.name)
        contentValues.put(CARD_FACE_ORACLE_TEXT, cardFace.oracle_text) // Missing Null Check
        contentValues.put(CARD_FACE_POWER, cardFace.power) // Missing Null Check
        contentValues.put(CARD_FACE_TOUGHNESS, cardFace.toughness) // Missing Null Check
        contentValues.put(CARD_FACE_TYPE_LINE, cardFace.type_line) // Missing Null Check
        contentValues.put(CARD_FACE_ARTIST, cardFace.artist) // Missing Null Check
        contentValues.put(CARD_FACE_FLAVOR_TEXT, cardFace.flavor_text) // Missing Null Check
        contentValues.put(
            CARD_FACE_IMAGE_PNG,
            if (cardFace.image_uris != null) cardFace.image_uris!!.png.toString() else null
        )
        contentValues.put(
            CARD_FACE_IMAGE_BORDER_CROP,
            if (cardFace.image_uris != null) cardFace.image_uris!!.border_crop.toString() else null
        )
        contentValues.put(
            CARD_FACE_IMAGE_ART_CROP,
            if (cardFace.image_uris != null) cardFace.image_uris!!.art_crop.toString() else null
        )
        contentValues.put(
            CARD_FACE_IMAGE_LARGE,
            if (cardFace.image_uris != null) cardFace.image_uris!!.large.toString() else null
        )
        contentValues.put(
            CARD_FACE_IMAGE_NORMAL,
            if (cardFace.image_uris != null) cardFace.image_uris!!.normal.toString() else null
        )
        contentValues.put(
            CARD_FACE_IMAGE_SMALL,
            if (cardFace.image_uris != null) cardFace.image_uris!!.small.toString() else null
        )
        val result = db.insert(CARD_FACE_TABLE_NAME, null, contentValues)
        return result
    }

    fun addRelatedCard(mainCardId: UUID, relatedCardId: UUID, componentType: String, name: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(RELATED_CARD_SCRYFALL_ID_MAIN, mainCardId.toString())
        contentValues.put(RELATED_CARD_SCRYFALL_ID_RELATED, relatedCardId.toString())
        contentValues.put(RELATED_CARD_COMPONENT, componentType)
        contentValues.put(RELATED_CARD_NAME, name)
        return db.insert(RELATED_CARD_TABLE_NAME, null, contentValues)
    }

    fun addArtistCatalog(name: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ARTIST_CATALOG_NAME, name)
        val result = db.insert(ARTIST_CATALOG_TABLE_NAME, null, contentValues)
        return result
    }

    fun addCreatureTypeCatalog(type: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CREATURE_TYPE_CATALOG_TYPE, type)
        val result = db.insert(CREATURE_TYPE_CATALOG_TABLE_NAME, null, contentValues)
        return result
    }

    fun addPlaneswalkerTypeCatalog(type: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(PLANESWALKER_TYPE_CATALOG_TYPE, type)
        val result = db.insert(PLANESWALKER_TYPE_CATALOG_TABLE_NAME, null, contentValues)
        return result
    }

    fun addLandTypeCatalog(type: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(LAND_TYPE_CATALOG_TYPE, type)
        val result = db.insert(LAND_TYPE_CATALOG_TABLE_NAME, null, contentValues)
        return result
    }

    fun addArtifactTypeCatalog(type: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ARTIFACT_TYPE_CATALOG_TYPE, type)
        val result = db.insert(ARTIFACT_TYPE_CATALOG_TABLE_NAME, null, contentValues)
        return result
    }

    fun addEnchantmentTypeCatalog(type: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ENCHANTMENT_TYPE_CATALOG_TYPE, type)
        val result = db.insert(ENCHANTMENT_TYPE_CATALOG_TABLE_NAME, null, contentValues)
        return result
    }

    fun addSpellTypeCatalog(type: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(SPELL_TYPE_CATALOG_TYPE, type)
        val result = db.insert(SPELL_TYPE_CATALOG_TABLE_NAME, null, contentValues)
        return result
    }

    fun addCardTypeCatalog(type: String, db: SQLiteDatabase): Long {
        val contentValues = ContentValues()
        contentValues.put(CARD_TYPE_CATALOG_TYPE, type)
        val result = db.insert(CARD_TYPE_CATALOG_TABLE_NAME, null, contentValues)
        return result
    }

    fun addCardSuperTypeCatalog(type: String, db: SQLiteDatabase): Long {
        val contentValues = ContentValues()
        contentValues.put(CARD_SUPER_TYPE_CATALOG_TYPE, type)
        val result = db.insert(CARD_SUPER_TYPE_CATALOG_TABLE_NAME, null, contentValues)
        return result
    }

    fun addCombo(combo: Combo): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COMBO_ID, combo.id)
        contentValues.put(COMBO_COLOR_IDENTITY, combo.colorIdentity!!.uppercase())
        contentValues.put(COMBO_PREREQUISITES, combo.prerequisites)
        contentValues.put(COMBO_STEPS, combo.steps)
        contentValues.put(COMBO_RESULTS, combo.results)
        val result = db.insert(COMBO_TABLE_NAME, null, contentValues)
        db.close()
        if (result > -1) {
            combo.cards!!.forEach {
                addCardInCombo (combo.id!!, it)
            }
        }
        return result
    }

    fun addCardInCombo(comboId: Int, cardId: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CIC_COMBO_ID, comboId)
        contentValues.put(CIC_CARD_NAME, cardId)
        val result = db.insert(CIC_TABLE_NAME, null, contentValues)
        return result
    }

    fun addDeck(deck: Deck, cardCommander: Array<String>?): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DECK_NAME, deck.name)
        contentValues.put(DECK_FORMAT, deck.format)
        contentValues.put(DECK_FACE_CARD_URI, deck.face_card_image_uri)
        contentValues.put(DECK_COLOR_IDENTITY, if (deck.colorIdentity.isNullOrEmpty()) "" else deck.colorIdentity)
        val result = db.insert(DECK_TABLE_NAME, null, contentValues)
        db.close()
        if (result > -1 && deck.format == "Commander" && !cardCommander.isNullOrEmpty()) cardCommander.forEach { addCardInDeck(result.toInt(), it, 1, "commander") }
        return result
    }

    fun addCardInDeck(deckId: Int, cardId: String, amount: Int, board: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CID_DECK_ID, deckId)
        contentValues.put(CID_CARD_ID, cardId)
        contentValues.put(CID_AMOUNT, amount)
        contentValues.put(CID_BOARD, board)
        return db.insert(CID_TABLE_NAME, null, contentValues)
    }

    private fun initialiseCardTypesAndSuperTypes (db: SQLiteDatabase) {
        val cardTypes = arrayOf("Artifact", "Conspiracy", "Creature", "Emblem", "Enchantment", "Hero", "Instant", "Land", "Phenomenon", "Plane", "Planeswalker", "Scheme", "Sorcery", "Tribal", "Vanguard")
        val cardSuperTypes = arrayOf("Basic", "Elite", "Legendary", "Ongoing", "Snow", "Token", "World")

        cardTypes.forEach {
            addCardTypeCatalog(it, db)
        }
        cardSuperTypes.forEach {
            addCardSuperTypeCatalog(it, db)
        }
    }

    @SuppressLint("Range")
    fun getDecks(): MutableList<Deck> {
        val query = "SELECT * FROM $DECK_TABLE_NAME"
        val list: MutableList<Deck> = ArrayList()
        val db = this.readableDatabase
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var currentDeck = Deck()
                currentDeck.id = result.getInt(result.getColumnIndex(DECK_ID))
                currentDeck.name = result.getString(result.getColumnIndex(DECK_NAME))
                currentDeck.format = result.getString(result.getColumnIndex(DECK_FORMAT))
                currentDeck.face_card_image_uri = result.getString(result.getColumnIndex(DECK_FACE_CARD_URI))
                currentDeck.colorIdentity = result.getString(result.getColumnIndex(DECK_COLOR_IDENTITY))
                list.add(currentDeck)
            } while (result.moveToNext())
        }
        return list
    }

    @SuppressLint("Range")
    fun getDeck(deckId: Int): Deck {
        val query = "SELECT * FROM $DECK_TABLE_NAME WHERE $DECK_ID == $deckId"
        var deck = Deck()
        val db = this.readableDatabase
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            deck.id = result.getInt(result.getColumnIndex(DECK_ID))
            deck.name = result.getString(result.getColumnIndex(DECK_NAME))
            deck.format = result.getString(result.getColumnIndex(DECK_FORMAT))
            deck.face_card_image_uri = result.getString(result.getColumnIndex(DECK_FACE_CARD_URI))
            deck.colorIdentity = result.getString(result.getColumnIndex(DECK_COLOR_IDENTITY))
        }
        return deck
    }

    @SuppressLint("Range")
    fun getDeckCards(deckId: Int): MutableList<Card> {
        val query = "SELECT $CID_CARD_ID FROM $CID_TABLE_NAME WHERE $CID_DECK_ID == $deckId"
        val db = this.readableDatabase
        val list: MutableList<Card> = ArrayList()
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                list.add(getCard(result.getString(result.getColumnIndex(CID_CARD_ID))))
            } while (result.moveToNext())
        }
        return list
    }

    @SuppressLint("Range")
    fun getAmountInDeck(deckId: Int, cardId: String): Int {
        val query = "SELECT $CID_AMOUNT FROM $CID_TABLE_NAME WHERE $CID_DECK_ID == $deckId AND $CID_CARD_ID == '$cardId'"
        val db = this.readableDatabase
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            return result.getInt(result.getColumnIndex(CID_AMOUNT))
        }
        return 0
    }

    @SuppressLint("Range")
    fun getDeckCardBoard(deckId: Int, cardId: String): String {
        val query = "SELECT $CID_BOARD FROM $CID_TABLE_NAME WHERE $CID_DECK_ID == $deckId AND $CID_CARD_ID == '$cardId'"
        val db = this.readableDatabase
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            return result.getString(result.getColumnIndex(CID_BOARD))
        }
        return "other"
    }

    @SuppressLint("Range")
    fun updateAmountInDeck(deckId: Int, cardId: String, amount: Int): Int {
        val db = this.writableDatabase
        if (amount < 1) return db.delete(CID_TABLE_NAME, "$CID_DECK_ID=$deckId AND $CID_CARD_ID='$cardId'", null)
        val contentValues = ContentValues()
        contentValues.put(CID_AMOUNT, amount)
        return db.update(CID_TABLE_NAME, contentValues, "$CID_DECK_ID=$deckId AND $CID_CARD_ID='$cardId'", null)
    }

    @SuppressLint("Range")
    fun updateColorIdentityInDeck(deckId: Int, identity: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DECK_COLOR_IDENTITY, identity)
        return db.update(DECK_TABLE_NAME, contentValues, "$DECK_ID=$deckId", null)
    }

    @SuppressLint("Range")
    fun updateFaceCardInDeck(deckId: Int, faceCardUri: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DECK_FACE_CARD_URI, faceCardUri)
        return db.update(DECK_TABLE_NAME, contentValues, "$DECK_ID=$deckId", null)
    }

    @SuppressLint("Range")
    fun updateDeckName(deckId: Int, name: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DECK_NAME, name)
        return db.update(DECK_TABLE_NAME, contentValues, "$DECK_ID=$deckId", null)
    }

    fun deleteDeck(deckId: Int): Int {
        val db = this.writableDatabase
        var result = db.delete(CID_TABLE_NAME, "$CID_DECK_ID=$deckId", null)
        if (result > -1) result = db.delete(DECK_TABLE_NAME, "$DECK_ID=$deckId", null)
        return result
    }

    @SuppressLint("Range")
    fun getCombos(cardNamesArray: Array<String>?, cardNamesAnd: Boolean, comboColorOperator: String?, comboColor: String?, comboResult: String?, page: Int): MutableList<Combo> {
        var whereStarted = false
        var query = "SELECT co.$COMBO_ID, co.$COMBO_COLOR_IDENTITY, co.$COMBO_RESULTS FROM $COMBO_TABLE_NAME co LEFT JOIN $CIC_TABLE_NAME cic ON co.$COMBO_ID == cic.$CIC_COMBO_ID"
        if (comboColor != null && comboColor != "") {
            if (comboColor == "C") query += if (!whereStarted) " WHERE (co.$COMBO_COLOR_IDENTITY == ''" else " AND (co.$COMBO_COLOR_IDENTITY == ''"
            else {
                when (comboColorOperator) {
                    "exactly" -> {
                        val colors = colorStringtoArray(comboColor)
                        val colorsNotSelected = mutableListOf("W", "U", "B", "R", "G")
                        var k = 0
                        while (k < colors.size) {
                            query += if (!whereStarted) " WHERE (co.$COMBO_COLOR_IDENTITY LIKE '%${colors[k]}%'" else if (k == 0) " AND (co.$COMBO_COLOR_IDENTITY LIKE '%${colors[k]}%'" else " AND co.$COMBO_COLOR_IDENTITY LIKE '%${colors[k]}%'"
                            colorsNotSelected.remove(colors[k])
                            whereStarted = true
                            ++k
                        }
                        k = 0
                        while (k < colorsNotSelected.size) {
                            query += if (k == 0) ") AND (co.$COMBO_COLOR_IDENTITY NOT LIKE '%${colorsNotSelected[k]}%'" else " AND co.$COMBO_COLOR_IDENTITY NOT LIKE '%${colorsNotSelected[k]}%'"
                            ++k
                        }
                    }
                    "including" -> {
                        val colors = colorStringtoArray(comboColor)
                        var k = 0
                        while (k < colors.size) {
                            query += if (!whereStarted) " WHERE (co.$COMBO_COLOR_IDENTITY LIKE '%${colors[k]}%'" else if (k == 0) " AND (co.$COMBO_COLOR_IDENTITY LIKE '%${colors[k]}%'" else " AND co.$COMBO_COLOR_IDENTITY LIKE '%${colors[k]}%'"
                            whereStarted = true
                            ++k
                        }
                    }
                    else -> {
                        val colors = colorStringtoArray(comboColor)
                        val colorsNotSelected = mutableListOf("W", "U", "B", "R", "G")
                        var k = 0
                        while (k < colors.size) {
                            query += if (!whereStarted) " WHERE (co.$COMBO_COLOR_IDENTITY LIKE '%${colors[k]}%'" else if (k == 0) " AND (co.$COMBO_COLOR_IDENTITY LIKE '%${colors[k]}%'" else " OR co.$COMBO_COLOR_IDENTITY LIKE '%${colors[k]}%'"
                            colorsNotSelected.remove(colors[k])
                            whereStarted = true
                            ++k
                        }
                        k = 0
                        while (k < colorsNotSelected.size) {
                            query += if (k == 0) ") AND (co.$COMBO_COLOR_IDENTITY NOT LIKE '%${colorsNotSelected[k]}%'" else " AND co.$COMBO_COLOR_IDENTITY NOT LIKE '%${colorsNotSelected[k]}%'"
                            ++k
                        }
                    }
                }
            }
            whereStarted = true
            query += ")"
        }
        if (comboResult != null && comboResult != "") {
            val words = comboResult.replace("'", "_").split(" ")
            var first = true
            words.forEach {
                query += if (!whereStarted) " WHERE (" else if (first) " AND (" else " AND"
                query += " co.$COMBO_RESULTS LIKE '%${it}%'"
                whereStarted = true
                first = false
            }
            query += ")"
        }
        if (!cardNamesArray.isNullOrEmpty()) {
            val subquery = query
            var first = true
            cardNamesArray.forEach {
                if (first) {
                    query += if (!whereStarted) " WHERE (" else " AND ("
                    query += " cic.$CIC_CARD_NAME LIKE '${it.replace("'", "_")}') GROUP BY $COMBO_ID"
                    first = false
                }
                else {
                    query += if (cardNamesAnd) " INTERSECT " else " UNION "
                    query += subquery
                    query += if (!whereStarted) " WHERE (" else " AND ("
                    query += " cic.$CIC_CARD_NAME LIKE '${it.replace("'", "_")}') GROUP BY $COMBO_ID"
                }
                whereStarted = true
            }
            query += "  ORDER BY $COMBO_ID LIMIT 50 OFFSET ${page * 50}"
        }
        else query += " GROUP BY $COMBO_ID ORDER BY $COMBO_ID LIMIT 50 OFFSET ${page * 50}"
        val list: MutableList<Combo> = ArrayList()
        val db = this.readableDatabase
        println(query)
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var currentCombo = Combo()
                var cardsInCombo: MutableList<String> = ArrayList()
                currentCombo.id = result.getInt(result.getColumnIndex(COMBO_ID))
                currentCombo.colorIdentity = result.getString(result.getColumnIndex(COMBO_COLOR_IDENTITY))
                currentCombo.results = result.getString(result.getColumnIndex(COMBO_RESULTS))
                query = "SELECT $CIC_CARD_NAME FROM $CIC_TABLE_NAME WHERE $CIC_COMBO_ID == ${currentCombo.id} ORDER BY $CIC_CARD_NAME"
                val res = db.rawQuery(query, null)
                if (res.moveToFirst()) {
                    do {
                        cardsInCombo.add(res.getString(res.getColumnIndex(CIC_CARD_NAME)))
                    } while (res.moveToNext())
                }
                currentCombo.cards = cardsInCombo.toTypedArray()
                list.add(currentCombo)
            } while (result.moveToNext())
        }
        return list
    }

    @SuppressLint("Range")
    fun getCombo(comboId: Int): Combo {
        var query = "SELECT * FROM $COMBO_TABLE_NAME WHERE $COMBO_ID == $comboId"
        val combo = Combo ()
        val db = this.readableDatabase
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            var cardsInCombo: MutableList<String> = ArrayList()
            combo.id = result.getInt(result.getColumnIndex(COMBO_ID))
            combo.colorIdentity = result.getString(result.getColumnIndex(COMBO_COLOR_IDENTITY))
            combo.prerequisites = result.getString(result.getColumnIndex(COMBO_PREREQUISITES))
            combo.steps = result.getString(result.getColumnIndex(COMBO_STEPS))
            combo.results = result.getString(result.getColumnIndex(COMBO_RESULTS))
            query = "SELECT $CIC_CARD_NAME FROM $CIC_TABLE_NAME WHERE $CIC_COMBO_ID == ${combo.id} ORDER BY $CIC_CARD_NAME"
            val res = db.rawQuery(query, null)
            if (res.moveToFirst()) {
                do {
                    cardsInCombo.add(res.getString(res.getColumnIndex(CIC_CARD_NAME)))
                } while (res.moveToNext())
            }
            combo.cards = cardsInCombo.toTypedArray()
        }
        return combo
    }

    @SuppressLint("Range")
    fun getComboCards(comboId: Int): MutableList<Card> {
        var query = "SELECT ca.$CARD_SCRYFALL_ID FROM $COMBO_TABLE_NAME co LEFT JOIN $CIC_TABLE_NAME cic ON co.$COMBO_ID == cic.$CIC_COMBO_ID" +
                " LEFT JOIN $CARD_TABLE_NAME ca ON cic.$CIC_CARD_NAME == ca.$CARD_NAME" +
                " WHERE $COMBO_ID == $comboId"
        val cardList: MutableList<Card> = ArrayList()
        val db = this.writableDatabase
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                cardList.add(getCard(result.getString(result.getColumnIndex(CARD_SCRYFALL_ID))))
            } while (result.moveToNext())
        }
        return cardList
    }

    @SuppressLint("Range")
    fun getCards(
        cardName: String?,
        cardTypes: Array<String>?,
        isCardTypes: BooleanArray?,
        cardTypesAnd: Boolean,
        cardText: String?,
        manaValueParamsArray: Array<String>?,
        powerParamsArray: Array<String>?,
        toughnessParamsArray: Array<String>?,
        loyaltyParamsArray: Array<String>?,
        rarityParamsArray: Array<String>?,
        legalityParamsArray: Array<String>?,
        layoutParamsArray: Array<String>?,
        manaCost: String?,
        cardColor: String?,
        colorOperator: String?,
        cardColorIdentity: String?,
        cardProducedMana: String?,
        cardFlavorText: String?,
        priceCoin: String?,
        priceOperator: String?,
        priceValue: String?,
        cardSet: String?,
        cardArtist: String?,
        similarToCardName: String?,
        page: Int
    ): MutableList<Card> {
        var whereStarted = false
        var similarSearch = false
        if (similarToCardName != null && similarToCardName != "") {
            similarWordTableInitialise (similarToCardName)
            similarSearch = true
        }
        var query = "SELECT * FROM $CARD_TABLE_NAME c LEFT JOIN $CARD_FACE_TABLE_NAME cf ON c.$CARD_SCRYFALL_ID == cf.$CARD_FACE_SCRYFALL_ID_MAIN_CARD"
        query += " WHERE c.$CARD_NAME NOT LIKE 'A-%'"
        whereStarted = true
        if (cardName != null && cardName != "") {
            query += if (whereStarted) " AND" else " WHERE"
            query += " c.$CARD_NAME LIKE '%${cardName.replace("'", "_")}%'"
            whereStarted = true
        }
        if (!cardTypes.isNullOrEmpty() && isCardTypes != null && isCardTypes.isNotEmpty()) {
            var k = 0
            while (k < cardTypes.size) {
                query += if (!whereStarted) " WHERE (" else if (k == 0) " AND (" else if (cardTypesAnd) " AND" else " OR"
                query += " c.$CARD_TYPE_LINE"
                if (!isCardTypes[k]) query += " NOT"
                query += " LIKE '%${cardTypes[k].replace("'", "_")}%'"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (cardText != null && cardText != "") {
            val words = cardText.replace("'", "_").split(" ")
            var first = true
            words.forEach {
                query += if (!whereStarted) " WHERE (" else if (first) " AND (" else " AND"
                query += " c.$CARD_ORACLE_TEXT LIKE '%${it}%'"
                whereStarted = true
                first = false
            }
            query += ")"
        }
        if (!manaValueParamsArray.isNullOrEmpty()) {
            var k = 0
            while (k < manaValueParamsArray.size) {
                query += if (!whereStarted) " WHERE (c.$CARD_CMC ${manaValueParamsArray[k]}" else if (k == 0) " AND (c.$CARD_CMC ${manaValueParamsArray[k]}" else if (k % 2 == 0) " c.$CARD_CMC ${manaValueParamsArray[k]}" else " ${manaValueParamsArray[k]}"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (!powerParamsArray.isNullOrEmpty()) {
            var k = 0
            while (k < powerParamsArray.size) {
                query += if (!whereStarted) " WHERE (c.$CARD_POWER ${powerParamsArray[k]}" else if (k == 0) " AND (c.$CARD_POWER ${powerParamsArray[k]}" else if (k % 2 == 0) " c.$CARD_POWER ${powerParamsArray[k]}" else " ${powerParamsArray[k]}"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (!toughnessParamsArray.isNullOrEmpty()) {
            var k = 0
            while (k < toughnessParamsArray.size) {
                query += if (!whereStarted) " WHERE (c.$CARD_TOUGHNESS ${toughnessParamsArray[k]}" else if (k == 0) " AND (c.$CARD_TOUGHNESS ${toughnessParamsArray[k]}" else if (k % 2 == 0) " c.$CARD_TOUGHNESS ${toughnessParamsArray[k]}" else " ${toughnessParamsArray[k]}"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (!loyaltyParamsArray.isNullOrEmpty()) {
            var k = 0
            while (k < loyaltyParamsArray.size) {
                query += if (!whereStarted) " WHERE (c.$CARD_LOYALTY ${loyaltyParamsArray[k]}" else if (k == 0) " AND (c.$CARD_LOYALTY ${loyaltyParamsArray[k]}" else if (k % 2 == 0) " c.$CARD_LOYALTY ${loyaltyParamsArray[k]}" else " ${loyaltyParamsArray[k]}"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (!rarityParamsArray.isNullOrEmpty()) {
            var k = 0
            while (k < rarityParamsArray.size) {
                query += if (!whereStarted) " WHERE (c.$CARD_RARITY == '${rarityParamsArray[k].lowercase()}'" else if (k == 0) " AND (c.$CARD_RARITY == '${rarityParamsArray[k].lowercase()}'" else if (k % 2 == 0) " c.$CARD_RARITY == '${rarityParamsArray[k].lowercase()}'" else " ${rarityParamsArray[k]}"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (!legalityParamsArray.isNullOrEmpty()) {
            var k = 0
            while (k < legalityParamsArray.size) {
                query += if (!whereStarted) " WHERE (c.legal_${legalityParamsArray[k].split(" ")[1].lowercase()} == '${
                    legalityParamsArray[k].split(
                        " "
                    )[0].lowercase()
                }'" else if (k == 0) " AND (c.legal_${legalityParamsArray[k].split(" ")[1].lowercase()} == '${
                    legalityParamsArray[k].split(
                        " "
                    )[0].lowercase()
                }'" else if (k % 2 == 0) " c.legal_${legalityParamsArray[k].split(" ")[1].lowercase()} == '${
                    legalityParamsArray[k].split(
                        " "
                    )[0].lowercase()
                }'" else " ${legalityParamsArray[k]}"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (!layoutParamsArray.isNullOrEmpty()) {
            var k = 0
            while (k < layoutParamsArray.size) {
                query += if (!whereStarted) " WHERE (c.$CARD_LAYOUT == '${
                    layoutParamsArray[k].lowercase().replace(" ", "_")
                }'" else if (k == 0) " AND (c.$CARD_LAYOUT == '${
                    layoutParamsArray[k].lowercase().replace(" ", "_")
                }'" else if (k % 2 == 0) " c.$CARD_LAYOUT == '${
                    layoutParamsArray[k].lowercase().replace(" ", "_")
                }'" else " ${layoutParamsArray[k]}"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        else {
            query += if (!whereStarted) " WHERE (" else " AND ("
            query += "NOT c.$CARD_LAYOUT == 'planar' AND NOT c.$CARD_LAYOUT == 'scheme' AND NOT c.$CARD_LAYOUT == 'vanguard' AND NOT c.$CARD_LAYOUT == 'token' AND NOT c.$CARD_LAYOUT == 'double_faced_token' AND NOT c.$CARD_LAYOUT == 'emblem' AND NOT c.$CARD_LAYOUT == 'art_series' AND NOT c.$CARD_LAYOUT == 'reversible_card'"
            query += ")"
            whereStarted = true
        }
        if (manaCost != null && manaCost != "") {
            var costStr = manaCost!!
            var first = true
            while (costStr.indexOf('{') != -1) {
                val subString = costStr.substring(costStr.indexOf('{'), costStr.indexOf('}') + 1)
                query += if (!whereStarted) " WHERE (" else if (first) " AND (" else " AND"
                val subStrCount = countSubString(costStr, subString)
                query += " c.$CARD_MANA_COST LIKE '%"
                var k = 0
                while (k < subStrCount) {
                    query += subString
                    ++k
                }
                query += "%'"
                costStr = costStr.replace(subString, "")
                whereStarted = true
                first = false
            }
            query += ")"
        }
        if (cardColor != null && cardColor != "") {
            if (cardColor == "C") query += if (!whereStarted) " WHERE (c.$CARD_COLORS == ''" else " AND (c.$CARD_COLORS == ''"
            else {
                when (colorOperator) {
                    "exactly" -> {
                        val colors = colorStringtoArray(cardColor)
                        val colorsNotSelected = mutableListOf("W", "U", "B", "R", "G")
                        var k = 0
                        while (k < colors.size) {
                            query += if (!whereStarted) " WHERE (c.$CARD_COLORS LIKE '%${colors[k]}%'" else if (k == 0) " AND (c.$CARD_COLORS LIKE '%${colors[k]}%'" else " AND c.$CARD_COLORS LIKE '%${colors[k]}%'"
                            colorsNotSelected.remove(colors[k])
                            whereStarted = true
                            ++k
                        }
                        k = 0
                        while (k < colorsNotSelected.size) {
                            query += if (k == 0) ") AND (c.$CARD_COLORS NOT LIKE '%${colorsNotSelected[k]}%'" else " AND c.$CARD_COLORS NOT LIKE '%${colorsNotSelected[k]}%'"
                            ++k
                        }
                    }
                    "including" -> {
                        val colors = colorStringtoArray(cardColor)
                        var k = 0
                        while (k < colors.size) {
                            query += if (!whereStarted) " WHERE (c.$CARD_COLORS LIKE '%${colors[k]}%'" else if (k == 0) " AND (c.$CARD_COLORS LIKE '%${colors[k]}%'" else " AND c.$CARD_COLORS LIKE '%${colors[k]}%'"
                            whereStarted = true
                            ++k
                        }
                    }
                    else -> {
                        val colors = colorStringtoArray(cardColor)
                        val colorsNotSelected = mutableListOf("W", "U", "B", "R", "G")
                        var k = 0
                        while (k < colors.size) {
                            query += if (!whereStarted) " WHERE (c.$CARD_COLORS LIKE '%${colors[k]}%'" else if (k == 0) " AND (c.$CARD_COLORS LIKE '%${colors[k]}%'" else " OR c.$CARD_COLORS LIKE '%${colors[k]}%'"
                            colorsNotSelected.remove(colors[k])
                            whereStarted = true
                            ++k
                        }
                        k = 0
                        while (k < colorsNotSelected.size) {
                            query += if (k == 0) ") AND (c.$CARD_COLORS NOT LIKE '%${colorsNotSelected[k]}%'" else " AND c.$CARD_COLORS NOT LIKE '%${colorsNotSelected[k]}%'"
                            ++k
                        }
                    }
                }
            }
            whereStarted = true
            query += ")"
        }
        if (cardColorIdentity != null && cardColorIdentity != "") {
            if (cardColorIdentity == "C") query += if (!whereStarted) " WHERE (c.$CARD_COLOR_IDENTITY == ''" else " AND (c.$CARD_COLOR_IDENTITY == ''"
            else {
                val colors = colorStringtoArray(cardColorIdentity)
                val colorsNotInIdentity = mutableListOf("W", "U", "B", "R", "G")
                var k = 0
                while (k < colors.size) {
                    query += if (!whereStarted) " WHERE (c.$CARD_COLOR_IDENTITY LIKE '%${colors[k]}%'" else if (k == 0) " AND (c.$CARD_COLOR_IDENTITY LIKE '%${colors[k]}%'" else " OR c.$CARD_COLOR_IDENTITY LIKE '%${colors[k]}%'"
                    colorsNotInIdentity.remove(colors[k])
                    whereStarted = true
                    ++k
                }
                k = 0
                while (k < colorsNotInIdentity.size) {
                    query += if (k == 0) ") AND (c.$CARD_COLOR_IDENTITY NOT LIKE '%${colorsNotInIdentity[k]}%'" else " AND c.$CARD_COLOR_IDENTITY NOT LIKE '%${colorsNotInIdentity[k]}%'"
                    ++k
                }
            }
            whereStarted = true
            query += ")"
        }
        if (cardProducedMana != null && cardProducedMana != "") {
            val colors = colorStringtoArray(cardProducedMana)
            var k = 0
            while (k < colors.size) {
                query += if (!whereStarted) " WHERE (c.$CARD_PRODUCED_MANA LIKE '%${colors[k]}%'" else if (k == 0) " AND (c.$CARD_PRODUCED_MANA LIKE '%${colors[k]}%'" else " AND c.$CARD_PRODUCED_MANA LIKE '%${colors[k]}%'"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (cardFlavorText != null && cardFlavorText != "") {
            val words = cardFlavorText.replace("'", "_").split(" ")
            var first = true
            words.forEach {
                query += if (!whereStarted) " WHERE (" else if (first) " AND (" else " AND"
                query += " c.$CARD_FLAVOR_TEXT LIKE '%${it}%'"
                whereStarted = true
                first = false
            }
            query += ")"
        }
        if (priceValue != null && priceValue != "") {
            query += if (!whereStarted) " WHERE (" else " AND ("
            query += if (priceCoin == "usd") "c.$CARD_PRICE_USD" else if (priceCoin == "eur") "c.$CARD_PRICE_EUR" else "c.$CARD_PRICE_TIX"
            query += " $priceOperator $priceValue"
            whereStarted = true
            query += if (priceCoin == "usd") " AND c.$CARD_PRICE_USD NOT NULL" else if (priceCoin == "eur") " AND c.$CARD_PRICE_EUR NOT NULL" else " AND c.$CARD_PRICE_TIX NOT NULL"
            query += ")"
        }
        if (cardSet != null && cardSet != "") {
            query += if (whereStarted) " AND" else " WHERE"
            query += " c.$CARD_SET_SCRYFALL_ID LIKE '%${getSetID(cardSet)}%'"
            whereStarted = true
        }
        if (cardArtist != null && cardArtist != "") {
            query += if (whereStarted) " AND" else " WHERE"
            query += " c.$CARD_ARTIST LIKE '%${cardArtist}%'"
            whereStarted = true
        }
        if (similarSearch) {
            query += " ORDER BY (SELECT COUNT (*) FROM similarCard s WHERE c.$CARD_ORACLE_TEXT LIKE '%' || s.textWord || '%') DESC"
        }
        else query += " ORDER BY $CARD_NAME"
        query += " LIMIT 50 OFFSET ${page * 50}"

        val list: MutableList<Card> = ArrayList()
        val db = this.readableDatabase
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var card = Card()
                card.id = UUID.fromString(result.getString(result.getColumnIndex(CARD_SCRYFALL_ID)))
                card.cmc = result.getFloat(result.getColumnIndex(CARD_CMC)).toDouble()
                card.color_identity = colorStringtoArray(result.getString(result.getColumnIndex(CARD_COLOR_IDENTITY)))
                card.colors = colorStringtoArray(result.getString(result.getColumnIndex(CARD_COLORS)))
                card.layout = result.getString(result.getColumnIndex(CARD_LAYOUT))
                card.loyalty = result.getStringOrNull(result.getColumnIndex(CARD_LOYALTY))
                card.mana_cost = result.getStringOrNull(result.getColumnIndex(CARD_MANA_COST))
                card.name = result.getString(result.getColumnIndex(CARD_NAME))
                card.oracle_text = result.getStringOrNull(result.getColumnIndex(CARD_ORACLE_TEXT))
                card.power = result.getStringOrNull(result.getColumnIndex(CARD_POWER))
                card.produced_mana = colorStringtoArray(result.getStringOrNull(result.getColumnIndex(CARD_PRODUCED_MANA)))
                card.toughness = result.getStringOrNull(result.getColumnIndex(CARD_TOUGHNESS))
                card.type_line = result.getString(result.getColumnIndex(CARD_TYPE_LINE))
                card.artist = result.getStringOrNull(result.getColumnIndex(CARD_ARTIST))
                card.flavor_text = result.getStringOrNull(result.getColumnIndex(CARD_FLAVOR_TEXT))
                card.rarity = result.getString(result.getColumnIndex(CARD_RARITY))
                card.set_id = result.getString(result.getColumnIndex(CARD_SET_SCRYFALL_ID))
                card.prices?.usd = result.getDoubleOrNull(result.getColumnIndex(CARD_PRICE_USD)).toString()
                card.prices?.eur = result.getDoubleOrNull(result.getColumnIndex(CARD_PRICE_EUR)).toString()
                card.prices?.tix = result.getDoubleOrNull(result.getColumnIndex(CARD_PRICE_TIX)).toString()
                card.legalities?.standard = result.getString(result.getColumnIndex(CARD_LEGAL_STANDARD))
                card.legalities?.pioneer = result.getString(result.getColumnIndex(CARD_LEGAL_PIONEER))
                card.legalities?.modern = result.getString(result.getColumnIndex(CARD_LEGAL_MODERN))
                card.legalities?.legacy = result.getString(result.getColumnIndex(CARD_LEGAL_LEGACY))
                card.legalities?.vintage = result.getString(result.getColumnIndex(CARD_LEGAL_VINTAGE))
                card.legalities?.brawl = result.getString(result.getColumnIndex(CARD_LEGAL_BRAWL))
                card.legalities?.historic = result.getString(result.getColumnIndex(CARD_LEGAL_HISTORIC))
                card.legalities?.pauper = result.getString(result.getColumnIndex(CARD_LEGAL_PAUPER))
                card.legalities?.penny = result.getString(result.getColumnIndex(CARD_LEGAL_PENNY))
                card.legalities?.commander = result.getString(result.getColumnIndex(CARD_LEGAL_COMMANDER))
                card.image_uris?.png = if (result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_PNG)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_PNG))) else null
                card.image_uris?.border_crop = if (result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_BORDER_CROP)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_BORDER_CROP))) else null
                card.image_uris?.art_crop = if (result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_ART_CROP)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_ART_CROP))) else null
                card.image_uris?.large = if (result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_LARGE)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_LARGE))) else null
                card.image_uris?.normal = if (result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_NORMAL)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_NORMAL))) else null
                card.image_uris?.small = if (result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_SMALL)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_SMALL))) else null
                val cardFaceName = result.getStringOrNull(result.getColumnIndex(CARD_FACE_NAME))
                if (cardFaceName != null) {
                    var cardFace = CardFace ()
                    cardFace.artist = result.getStringOrNull(result.getColumnIndex(CARD_FACE_ARTIST))
                    cardFace.cmc = result.getFloat(result.getColumnIndex(CARD_FACE_CMC)).toDouble()
                    cardFace.colors = colorStringtoArray(result.getString(result.getColumnIndex(CARD_FACE_COLORS)))
                    cardFace.flavor_text = result.getStringOrNull(result.getColumnIndex(CARD_FACE_FLAVOR_TEXT))
                    cardFace.image_uris?.png = if (result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_PNG)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_PNG))) else null
                    cardFace.image_uris?.border_crop = if (result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_BORDER_CROP)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_BORDER_CROP))) else null
                    cardFace.image_uris?.art_crop = if (result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_ART_CROP)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_ART_CROP))) else null
                    cardFace.image_uris?.large = if (result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_LARGE)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_LARGE))) else null
                    cardFace.image_uris?.normal = if (result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_NORMAL)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_NORMAL))) else null
                    cardFace.image_uris?.small = if (result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_SMALL)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_SMALL))) else null
                    cardFace.layout = result.getStringOrNull(result.getColumnIndex(CARD_FACE_LAYOUT))
                    cardFace.loyalty = result.getStringOrNull(result.getColumnIndex(CARD_FACE_LOYALTY))
                    cardFace.mana_cost = result.getStringOrNull(result.getColumnIndex(CARD_FACE_MANA_COST))
                    cardFace.name = result.getStringOrNull(result.getColumnIndex(CARD_FACE_NAME))
                    cardFace.oracle_text = result.getStringOrNull(result.getColumnIndex(CARD_FACE_ORACLE_TEXT))
                    cardFace.power = result.getStringOrNull(result.getColumnIndex(CARD_FACE_POWER))
                    cardFace.toughness = result.getStringOrNull(result.getColumnIndex(CARD_FACE_TOUGHNESS))
                    cardFace.type_line = result.getStringOrNull(result.getColumnIndex(CARD_FACE_TYPE_LINE))
                    card.card_faces = arrayOf(cardFace)
                }
                list.add(card)
            } while (result.moveToNext())
        }
        return list
    }

    @SuppressLint("Range")
    fun getCard (scryfallId: String): Card {
        val query = "SELECT * " +
                "FROM $CARD_TABLE_NAME c LEFT JOIN $CARD_FACE_TABLE_NAME cf ON c.$CARD_SCRYFALL_ID == cf.$CARD_FACE_SCRYFALL_ID_MAIN_CARD " +
                "LEFT JOIN $RELATED_CARD_TABLE_NAME cr ON c.$CARD_SCRYFALL_ID == cr.$RELATED_CARD_SCRYFALL_ID_MAIN " +
                "WHERE c.$CARD_SCRYFALL_ID == '$scryfallId'"
        var card = Card()
        val cardFaces: MutableList<CardFace> = ArrayList()
        val relatedCards: MutableList<RelatedCard> = ArrayList()
        val db = readableDatabase
        val result = db.rawQuery(query, null)
        var first = true
        if (result.moveToFirst()) {
            do {
                if (first) {
                    card.id = UUID.fromString(result.getString(result.getColumnIndex(CARD_SCRYFALL_ID)))
                    card.cmc = result.getFloat(result.getColumnIndex(CARD_CMC)).toDouble()
                    card.color_identity = colorStringtoArray(result.getString(result.getColumnIndex(CARD_COLOR_IDENTITY)))
                    card.colors = colorStringtoArray(result.getString(result.getColumnIndex(CARD_COLORS)))
                    card.layout = result.getString(result.getColumnIndex(CARD_LAYOUT))
                    card.loyalty = result.getStringOrNull(result.getColumnIndex(CARD_LOYALTY))
                    card.mana_cost = result.getStringOrNull(result.getColumnIndex(CARD_MANA_COST))
                    card.name = result.getString(result.getColumnIndex(CARD_NAME))
                    card.oracle_text = result.getStringOrNull(result.getColumnIndex(CARD_ORACLE_TEXT))
                    card.power = result.getStringOrNull(result.getColumnIndex(CARD_POWER))
                    card.produced_mana = colorStringtoArray(result.getStringOrNull(result.getColumnIndex(CARD_PRODUCED_MANA)))
                    card.toughness = result.getStringOrNull(result.getColumnIndex(CARD_TOUGHNESS))
                    card.type_line = result.getString(result.getColumnIndex(CARD_TYPE_LINE))
                    card.artist = result.getStringOrNull(result.getColumnIndex(CARD_ARTIST))
                    card.flavor_text = result.getStringOrNull(result.getColumnIndex(CARD_FLAVOR_TEXT))
                    card.rarity = result.getString(result.getColumnIndex(CARD_RARITY))
                    card.set_id = result.getString(result.getColumnIndex(CARD_SET_SCRYFALL_ID))
                    card.prices?.usd = result.getDoubleOrNull(result.getColumnIndex(CARD_PRICE_USD)).toString()
                    card.prices?.eur = result.getDoubleOrNull(result.getColumnIndex(CARD_PRICE_EUR)).toString()
                    card.prices?.tix = result.getDoubleOrNull(result.getColumnIndex(CARD_PRICE_TIX)).toString()
                    card.legalities?.standard = result.getString(result.getColumnIndex(CARD_LEGAL_STANDARD))
                    card.legalities?.pioneer = result.getString(result.getColumnIndex(CARD_LEGAL_PIONEER))
                    card.legalities?.modern = result.getString(result.getColumnIndex(CARD_LEGAL_MODERN))
                    card.legalities?.legacy = result.getString(result.getColumnIndex(CARD_LEGAL_LEGACY))
                    card.legalities?.vintage = result.getString(result.getColumnIndex(CARD_LEGAL_VINTAGE))
                    card.legalities?.brawl = result.getString(result.getColumnIndex(CARD_LEGAL_BRAWL))
                    card.legalities?.historic = result.getString(result.getColumnIndex(CARD_LEGAL_HISTORIC))
                    card.legalities?.pauper = result.getString(result.getColumnIndex(CARD_LEGAL_PAUPER))
                    card.legalities?.penny = result.getString(result.getColumnIndex(CARD_LEGAL_PENNY))
                    card.legalities?.commander = result.getString(result.getColumnIndex(CARD_LEGAL_COMMANDER))
                    card.image_uris?.png = if (result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_PNG)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_PNG))) else null
                    card.image_uris?.border_crop = if (result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_BORDER_CROP)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_BORDER_CROP))) else null
                    card.image_uris?.art_crop = if (result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_ART_CROP)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_ART_CROP))) else null
                    card.image_uris?.large = if (result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_LARGE)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_LARGE))) else null
                    card.image_uris?.normal = if (result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_NORMAL)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_NORMAL))) else null
                    card.image_uris?.small = if (result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_SMALL)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_IMAGE_SMALL))) else null
                    first = false
                }
                val cardFaceName = result.getStringOrNull(result.getColumnIndex(CARD_FACE_NAME))
                if (cardFaceName != null) {
                    var cardFace = CardFace()
                    cardFace.artist = result.getStringOrNull(result.getColumnIndex(CARD_FACE_ARTIST))
                    cardFace.cmc = result.getFloat(result.getColumnIndex(CARD_FACE_CMC)).toDouble()
                    cardFace.colors = colorStringtoArray(result.getString(result.getColumnIndex(CARD_FACE_COLORS)))
                    cardFace.flavor_text = result.getStringOrNull(result.getColumnIndex(CARD_FACE_FLAVOR_TEXT))
                    cardFace.image_uris?.png = if (result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_PNG)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_PNG))) else null
                    cardFace.image_uris?.border_crop = if (result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_BORDER_CROP)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_BORDER_CROP))) else null
                    cardFace.image_uris?.art_crop = if (result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_ART_CROP)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_ART_CROP))) else null
                    cardFace.image_uris?.large = if (result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_LARGE)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_LARGE))) else null
                    cardFace.image_uris?.normal = if (result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_NORMAL)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_NORMAL))) else null
                    cardFace.image_uris?.small = if (result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_SMALL)) != null) URI.create(result.getStringOrNull(result.getColumnIndex(CARD_FACE_IMAGE_SMALL))) else null
                    cardFace.layout = result.getStringOrNull(result.getColumnIndex(CARD_FACE_LAYOUT))
                    cardFace.loyalty = result.getStringOrNull(result.getColumnIndex(CARD_FACE_LOYALTY))
                    cardFace.mana_cost = result.getStringOrNull(result.getColumnIndex(CARD_FACE_MANA_COST))
                    cardFace.name = result.getStringOrNull(result.getColumnIndex(CARD_FACE_NAME))
                    cardFace.oracle_text = result.getStringOrNull(result.getColumnIndex(CARD_FACE_ORACLE_TEXT))
                    cardFace.power = result.getStringOrNull(result.getColumnIndex(CARD_FACE_POWER))
                    cardFace.toughness = result.getStringOrNull(result.getColumnIndex(CARD_FACE_TOUGHNESS))
                    cardFace.type_line = result.getStringOrNull(result.getColumnIndex(CARD_FACE_TYPE_LINE))
                    var found = false
                    cardFaces.forEach { if (it.name == cardFace.name) found = true }
                    if (!found) cardFaces.add(cardFace)
                }
                val relatedCardScryfallIdRelated = result.getStringOrNull(result.getColumnIndex(RELATED_CARD_SCRYFALL_ID_RELATED))
                if (relatedCardScryfallIdRelated != null) {
                    var relatedCard = RelatedCard()
                    relatedCard.id = UUID.fromString(relatedCardScryfallIdRelated)
                    relatedCard.component = result.getStringOrNull(result.getColumnIndex(RELATED_CARD_COMPONENT))
                    relatedCard.name = result.getStringOrNull(result.getColumnIndex(RELATED_CARD_NAME))
                    var found = false
                    relatedCards.forEach { if (it.name == relatedCard.name) found = true }
                    if (!found) relatedCards.add(relatedCard)
                }
            } while (result.moveToNext())
            card.card_faces = cardFaces.toTypedArray()
            card.all_parts = relatedCards.toTypedArray()
        }
        return card
    }

    fun getSetID (name: String): String {
        var id = ""
        val db = this.readableDatabase
        val query = "SELECT $CARD_SET_SCRYFALL_SET_ID FROM $CARD_SET_TABLE_NAME WHERE $CARD_SET_NAME LIKE '%$name%' ORDER BY $CARD_SET_NAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            id = result.getStringOrNull(result.getColumnIndex(CARD_SET_SCRYFALL_SET_ID))!!
        }
        return id
    }

    fun getCardIdFromName (name: String): String {
        var id = ""
        val db = this.readableDatabase
        val query = "SELECT $CARD_SCRYFALL_ID FROM $CARD_TABLE_NAME WHERE $CARD_NAME LIKE '${name.replace("'", "_")}'"
        println(query)
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            id = result.getStringOrNull(result.getColumnIndex(CARD_SCRYFALL_ID))!!
        }
        return id
    }

    fun getAllTypeCatalogs (): MutableList<String> {
        val list: MutableList<String> = ArrayList()
        val db = this.readableDatabase
        var query = "SELECT * FROM $CARD_TYPE_CATALOG_TABLE_NAME ORDER BY $CARD_TYPE_CATALOG_TYPE"
        var result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val type = result.getStringOrNull(result.getColumnIndex(CARD_TYPE_CATALOG_TYPE))
                if (type != null) list.add(type)
            } while (result.moveToNext())
        }
        query = "SELECT * FROM $CARD_SUPER_TYPE_CATALOG_TABLE_NAME ORDER BY $CARD_SUPER_TYPE_CATALOG_TYPE"
        result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val type = result.getStringOrNull(result.getColumnIndex(CARD_SUPER_TYPE_CATALOG_TYPE))
                if (type != null) list.add(type)
            } while (result.moveToNext())
        }
        query = "SELECT * FROM $CREATURE_TYPE_CATALOG_TABLE_NAME ORDER BY $CREATURE_TYPE_CATALOG_TYPE"
        result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val type = result.getStringOrNull(result.getColumnIndex(CREATURE_TYPE_CATALOG_TYPE))
                if (type != null) list.add(type)
            } while (result.moveToNext())
        }
        query = "SELECT * FROM $LAND_TYPE_CATALOG_TABLE_NAME ORDER BY $LAND_TYPE_CATALOG_TYPE"
        result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val type = result.getStringOrNull(result.getColumnIndex(LAND_TYPE_CATALOG_TYPE))
                if (type != null) list.add(type)
            } while (result.moveToNext())
        }
        query = "SELECT * FROM $PLANESWALKER_TYPE_CATALOG_TABLE_NAME ORDER BY $PLANESWALKER_TYPE_CATALOG_TYPE"
        result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val type = result.getStringOrNull(result.getColumnIndex(PLANESWALKER_TYPE_CATALOG_TYPE))
                if (type != null) list.add(type)
            } while (result.moveToNext())
        }
        query = "SELECT * FROM $ARTIFACT_TYPE_CATALOG_TABLE_NAME ORDER BY $ARTIFACT_TYPE_CATALOG_TYPE"
        result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val type = result.getStringOrNull(result.getColumnIndex(ARTIFACT_TYPE_CATALOG_TYPE))
                if (type != null) list.add(type)
            } while (result.moveToNext())
        }
        query = "SELECT * FROM $ENCHANTMENT_TYPE_CATALOG_TABLE_NAME ORDER BY $ENCHANTMENT_TYPE_CATALOG_TYPE"
        result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val type = result.getStringOrNull(result.getColumnIndex(ENCHANTMENT_TYPE_CATALOG_TYPE))
                if (type != null) list.add(type)
            } while (result.moveToNext())
        }
        query = "SELECT * FROM $SPELL_TYPE_CATALOG_TABLE_NAME ORDER BY $SPELL_TYPE_CATALOG_TYPE"
        result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val type = result.getStringOrNull(result.getColumnIndex(SPELL_TYPE_CATALOG_TYPE))
                if (type != null) list.add(type)
            } while (result.moveToNext())
        }
        return list
    }

    fun getArtistCatalog (): MutableList<String> {
        val list: MutableList<String> = ArrayList()
        val db = this.readableDatabase
        var query = "SELECT * FROM $ARTIST_CATALOG_TABLE_NAME ORDER BY $ARTIST_CATALOG_NAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val artistName = result.getStringOrNull(result.getColumnIndex(ARTIST_CATALOG_NAME))
                if (artistName != null) list.add(artistName)
            } while (result.moveToNext())
        }
        return list
    }

    fun getSetCatalog (): MutableList<String> {
        val list: MutableList<String> = ArrayList()
        val db = this.readableDatabase
        var query = "SELECT $CARD_SET_NAME FROM $CARD_SET_TABLE_NAME ORDER BY $CARD_SET_NAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val setName = result.getStringOrNull(result.getColumnIndex(CARD_SET_NAME))
                if (setName != null) list.add(setName)
            } while (result.moveToNext())
        }
        return list
    }

    fun getCardNameCatalog (str: String): MutableList<String> {
        val list: MutableList<String> = ArrayList()
        val db = this.readableDatabase
        var query = "SELECT $CARD_NAME FROM $CARD_TABLE_NAME WHERE $CARD_NAME LIKE '%${str.replace("'", "_")}%' AND NOT $CARD_LAYOUT == 'planar' AND NOT $CARD_LAYOUT == 'scheme' AND NOT $CARD_LAYOUT == 'vanguard' AND NOT $CARD_LAYOUT == 'token' AND NOT $CARD_LAYOUT == 'double_faced_token' AND NOT $CARD_LAYOUT == 'emblem' AND NOT $CARD_LAYOUT == 'art_series' AND NOT $CARD_LAYOUT == 'reversible_card' ORDER BY $CARD_NAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val name = result.getStringOrNull(result.getColumnIndex(CARD_NAME))
                if (name != null) list.add(name)
            } while (result.moveToNext())
        }
        return list
    }
    fun isCardNameValid (str: String): Boolean {
        var res = false
        val db = this.readableDatabase
        var query = "SELECT $CARD_NAME FROM $CARD_TABLE_NAME WHERE $CARD_NAME LIKE '%${str.replace("'", "_")}%' AND NOT $CARD_LAYOUT == 'planar' AND NOT $CARD_LAYOUT == 'scheme' AND NOT $CARD_LAYOUT == 'vanguard' AND NOT $CARD_LAYOUT == 'token' AND NOT $CARD_LAYOUT == 'double_faced_token' AND NOT $CARD_LAYOUT == 'emblem' AND NOT $CARD_LAYOUT == 'art_series' AND NOT $CARD_LAYOUT == 'reversible_card' ORDER BY $CARD_NAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val name = result.getStringOrNull(result.getColumnIndex(CARD_NAME))
                if (name?.lowercase() == str.lowercase()) res = true
            } while (result.moveToNext())
        }
        return res
    }

    private fun colorArrayToString(colors: Array<String>?): String {
        var result = if (colors.isNullOrEmpty()) "" else colors[0]
        var i = 1
        while (result != "" && i < colors?.size!!) {
            result += "," + colors[i]
            i++
        }
        return result
    }

    private fun colorStringtoArray(colors: String?): Array<String> {
        var result = arrayOf<String>()
        if (colors == "") return result
        if (colors != null) result = colors.split(",").toTypedArray()
        return result
    }

    fun stringToSpannableString(string: String, textSize: Int): SpannableString {
        var str = string
        var result = SpannableString(str)
        while (str.indexOf('{') != -1) {
            val subStart = str.indexOf('{')
            val subEnd = str.indexOf('}')
            val subString = str.substring(subStart, subEnd + 1)
            val draw = getSymbolDrawable(subString.uppercase())
            val proportion = draw.intrinsicWidth / draw.intrinsicHeight
            draw.setBounds(0, 0, textSize * proportion, textSize)
            val imageSpan = ImageSpan(draw, ImageSpan.ALIGN_BOTTOM)
            result.setSpan(imageSpan, subStart, subEnd + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            str = str.replaceFirst('{', '0')
            str = str.replaceFirst('}', '0')
            println(str)
        }
        return result
    }

    private fun similarWordTableInitialise (cardName: String) {
        var query = "DROP TABLE IF EXISTS similarCard"
        val dbW = this.writableDatabase
        dbW.execSQL(query)
        query = "CREATE TABLE similarCard (textWord text)"
        dbW.execSQL(query)
        query = "SELECT $CARD_ORACLE_TEXT FROM $CARD_TABLE_NAME WHERE $CARD_NAME LIKE '${cardName.replace("'", "_")}'"
        val db = this.readableDatabase
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            val words = result.getStringOrNull(result.getColumnIndex(CARD_ORACLE_TEXT))?.split(" ")
            words?.forEach {
                val contentValues = ContentValues()
                contentValues.put("textWord", it.lowercase().replace(".", "").replace(":", "").replace(",", "").replace("'", "_"))
                val res = dbW.insert("similarCard", null, contentValues)
                println(res.toString() + "   " + it.lowercase().replace(".", "").replace(":", "").replace(",", "").replace("'", "_"))
            }
        }
        dbW.close()
        db.close()
    }

    fun getSymbolDrawable(str: String): Drawable {
        when (str) {
            "{T}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_t_cost)!!
            "{Q}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_q_cost)!!
            "{E}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_e_cost)!!
            "{PW}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_pw_cost)!!
            "{CHAOS}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_chaos_cost
            )!!
            "{A}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_a_cost)!!
            "{X}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_x_cost)!!
            "{Y}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_y_cost)!!
            "{Z}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_z_cost)!!
            "{0}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_0_cost)!!
            "{1}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_1_cost)!!
            "{2}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_2_cost)!!
            "{3}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_3_cost)!!
            "{4}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_4_cost)!!
            "{5}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_5_cost)!!
            "{6}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_6_cost)!!
            "{7}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_7_cost)!!
            "{8}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_8_cost)!!
            "{9}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_9_cost)!!
            "{10}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_10_cost)!!
            "{11}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_11_cost)!!
            "{12}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_12_cost)!!
            "{13}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_13_cost)!!
            "{14}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_14_cost)!!
            "{15}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_15_cost)!!
            "{16}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_16_cost)!!
            "{17}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_17_cost)!!
            "{18}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_18_cost)!!
            "{19}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_19_cost)!!
            "{20}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_20_cost)!!
            "{100}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_100_cost
            )!!
            "{1000000}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_1000000_cost
            )!!
            "{???}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_infinity_cost
            )!!
            "{W/U}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_wu_cost)!!
            "{W/B}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_wb_cost)!!
            "{B/R}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_br_cost)!!
            "{B/G}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_bg_cost)!!
            "{U/B}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_ub_cost)!!
            "{U/R}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_ur_cost)!!
            "{R/G}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_rg_cost)!!
            "{R/W}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_rw_cost)!!
            "{G/W}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_gw_cost)!!
            "{G/U}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_gu_cost)!!
            "{W/U/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_wup_cost
            )!!
            "{W/B/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_wbp_cost
            )!!
            "{B/R/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_brp_cost
            )!!
            "{B/G/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_bgp_cost
            )!!
            "{U/B/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_ubp_cost
            )!!
            "{U/R/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_urp_cost
            )!!
            "{R/G/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_rgp_cost
            )!!
            "{R/W/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_rwp_cost
            )!!
            "{G/W/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_gwp_cost
            )!!
            "{G/U/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_gup_cost
            )!!
            "{2/W}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_2w_cost)!!
            "{2/U}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_2u_cost)!!
            "{2/B}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_2b_cost)!!
            "{2/R}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_2r_cost)!!
            "{2/G}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_2g_cost)!!
            "{W}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_w_cost)!!
            "{U}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_u_cost)!!
            "{B}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_b_cost)!!
            "{R}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_r_cost)!!
            "{G}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_g_cost)!!
            "{P}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_p_cost)!!
            "{W/P}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_wp_cost)!!
            "{U/P}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_up_cost)!!
            "{B/P}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_bp_cost)!!
            "{R/P}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_rp_cost)!!
            "{G/P}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_gp_cost)!!
            "{C}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_c_cost)!!
            "{S}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_s_cost)!!
            else -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_1_2_cost)!!
        }
    }

    fun countSubString(str: String, subStr: String): Int {
        return str.split(subStr).size - 1
    }
}