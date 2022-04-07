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
import androidx.core.database.getStringOrNull
import magicchief.main.brewersservant.dataclass.Card
import magicchief.main.brewersservant.dataclass.CardFace
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
                        + CARD_ID + " int auto_increment primary key,"
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
                        + CARD_FACE_ID + " int auto_increment primary key,"
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
                        + RELATED_CARD_ID + " int auto_increment primary key,"
                        + RELATED_CARD_SCRYFALL_ID_MAIN + " char(36) not null,"
                        + RELATED_CARD_SCRYFALL_ID_RELATED + " char(36) not null,"
                        + RELATED_CARD_COMPONENT + " varchar(25) not null,"
                        + "foreign key ($RELATED_CARD_SCRYFALL_ID_MAIN) references $CARD_TABLE_NAME($CARD_SCRYFALL_ID),"
                        + "foreign key ($RELATED_CARD_SCRYFALL_ID_RELATED) references $CARD_TABLE_NAME($CARD_SCRYFALL_ID)"
                        + ")"
                )
        // Create CardSet table
        val createCardSetDB = (
                "CREATE TABLE $CARD_SET_TABLE_NAME ("
                        + CARD_SET_SET_ID + " int auto_increment primary key,"
                        + CARD_SET_SCRYFALL_SET_ID + " char(36) not null,"
                        + CARD_SET_CODE + " varchar(10) not null,"
                        + CARD_SET_NAME + " varchar(50) not null,"
                        + CARD_SET_ICON_SVG_URI + " varchar(255) not null"
                        + ")"
                )
        // Create Catalog tables
        val createArtistCatalogDB = (
                "CREATE TABLE $ARTIST_CATALOG_TABLE_NAME ("
                        + ARTIST_CATALOG_ID + " int auto_increment primary key,"
                        + ARTIST_CATALOG_NAME + " varchar(50) not null"
                        + ")"
                )
        val createCreatureTypeCatalogDB = (
                "CREATE TABLE $CREATURE_TYPE_CATALOG_TABLE_NAME ("
                        + CREATURE_TYPE_CATALOG_ID + " int auto_increment primary key,"
                        + CREATURE_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createPlaneswalkerTypeCatalogDB = (
                "CREATE TABLE $PLANESWALKER_TYPE_CATALOG_TABLE_NAME ("
                        + PLANESWALKER_TYPE_CATALOG_ID + " int auto_increment primary key,"
                        + PLANESWALKER_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createLandTypeCatalogDB = (
                "CREATE TABLE $LAND_TYPE_CATALOG_TABLE_NAME ("
                        + LAND_TYPE_CATALOG_ID + " int auto_increment primary key,"
                        + LAND_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createArtifactTypeCatalogDB = (
                "CREATE TABLE $ARTIFACT_TYPE_CATALOG_TABLE_NAME ("
                        + ARTIFACT_TYPE_CATALOG_ID + " int auto_increment primary key,"
                        + ARTIFACT_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createEnchantmentTypeCatalogDB = (
                "CREATE TABLE $ENCHANTMENT_TYPE_CATALOG_TABLE_NAME ("
                        + ENCHANTMENT_TYPE_CATALOG_ID + " int auto_increment primary key,"
                        + ENCHANTMENT_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createSpellTypeCatalogDB = (
                "CREATE TABLE $SPELL_TYPE_CATALOG_TABLE_NAME ("
                        + SPELL_TYPE_CATALOG_ID + " int auto_increment primary key,"
                        + SPELL_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createCardTypeCatalogDB = (
                "CREATE TABLE $CARD_TYPE_CATALOG_TABLE_NAME ("
                        + CARD_TYPE_CATALOG_ID + " int auto_increment primary key,"
                        + CARD_TYPE_CATALOG_TYPE + " varchar(50) not null"
                        + ")"
                )
        val createCardSuperTypeCatalogDB = (
                "CREATE TABLE $CARD_SUPER_TYPE_CATALOG_TABLE_NAME ("
                        + CARD_SUPER_TYPE_CATALOG_ID + " int auto_increment primary key,"
                        + CARD_SUPER_TYPE_CATALOG_TYPE + " varchar(50) not null"
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

        initialiseCardTypesAndSuperTypes (db)

    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // This method drops the tables if they existed and creates them again
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
        private val CARD_ID = "id"
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
        private val CARD_FACE_ID = "id"
        private val CARD_FACE_SCRYFALL_ID_MAIN_CARD = "id_main_card_scryfall"
        private val CARD_FACE_ARTIST = "artist"
        private val CARD_FACE_CMC = "cmc"
        private val CARD_FACE_COLORS = "colors"
        private val CARD_FACE_FLAVOR_TEXT = "flavor_text"
        private val CARD_FACE_LAYOUT = "layout"
        private val CARD_FACE_LOYALTY = "loyalty"
        private val CARD_FACE_MANA_COST = "mana_cost"
        private val CARD_FACE_NAME = "name"
        private val CARD_FACE_ORACLE_TEXT = "oracle_text"
        private val CARD_FACE_POWER = "power"
        private val CARD_FACE_TOUGHNESS = "toughness"
        private val CARD_FACE_TYPE_LINE = "type_line"
        private val CARD_FACE_IMAGE_PNG = "image_png"
        private val CARD_FACE_IMAGE_BORDER_CROP = "image_border_crop"
        private val CARD_FACE_IMAGE_ART_CROP = "image_art_crop"
        private val CARD_FACE_IMAGE_LARGE = "image_large"
        private val CARD_FACE_IMAGE_NORMAL = "image_normal"
        private val CARD_FACE_IMAGE_SMALL = "image_small"

        // RelatedCard table name
        private val RELATED_CARD_TABLE_NAME = "RelatedCard"

        // RelatedCard table column names
        private val RELATED_CARD_ID = "id"
        private val RELATED_CARD_SCRYFALL_ID_MAIN = "id_main_scryfall"
        private val RELATED_CARD_SCRYFALL_ID_RELATED = "id_related_scryfall"
        private val RELATED_CARD_COMPONENT = "component"

        // CardSet table name
        private val CARD_SET_TABLE_NAME = "CardSet"

        // CardSet table column names
        private val CARD_SET_SET_ID = "id"
        private val CARD_SET_SCRYFALL_SET_ID = "scryfall_set_id"
        private val CARD_SET_CODE = "code"
        private val CARD_SET_NAME = "name"
        private val CARD_SET_ICON_SVG_URI = "icon_svg_uri"

        // Catalog Tables
        private val ARTIST_CATALOG_TABLE_NAME = "ArtistCatalog"
        private val ARTIST_CATALOG_ID = "id"
        private val ARTIST_CATALOG_NAME = "name"

        private val CREATURE_TYPE_CATALOG_TABLE_NAME = "CreatureTypeCatalog"
        private val CREATURE_TYPE_CATALOG_ID = "id"
        private val CREATURE_TYPE_CATALOG_TYPE = "type"

        private val PLANESWALKER_TYPE_CATALOG_TABLE_NAME = "PlaneswalkerTypeCatalog"
        private val PLANESWALKER_TYPE_CATALOG_ID = "id"
        private val PLANESWALKER_TYPE_CATALOG_TYPE = "type"

        private val LAND_TYPE_CATALOG_TABLE_NAME = "LandTypeCatalog"
        private val LAND_TYPE_CATALOG_ID = "id"
        private val LAND_TYPE_CATALOG_TYPE = "type"

        private val ARTIFACT_TYPE_CATALOG_TABLE_NAME = "ArtifactTypeCatalog"
        private val ARTIFACT_TYPE_CATALOG_ID = "id"
        private val ARTIFACT_TYPE_CATALOG_TYPE = "type"

        private val ENCHANTMENT_TYPE_CATALOG_TABLE_NAME = "EnchantmentTypeCatalog"
        private val ENCHANTMENT_TYPE_CATALOG_ID = "id"
        private val ENCHANTMENT_TYPE_CATALOG_TYPE = "type"

        private val SPELL_TYPE_CATALOG_TABLE_NAME = "SpellTypeCatalog"
        private val SPELL_TYPE_CATALOG_ID = "id"
        private val SPELL_TYPE_CATALOG_TYPE = "type"

        private val CARD_TYPE_CATALOG_TABLE_NAME = "CardTypeCatalog"
        private val CARD_TYPE_CATALOG_ID = "id"
        private val CARD_TYPE_CATALOG_TYPE = "type"

        private val CARD_SUPER_TYPE_CATALOG_TABLE_NAME = "CardSuperTypeCatalog"
        private val CARD_SUPER_TYPE_CATALOG_ID = "id"
        private val CARD_SUPER_TYPE_CATALOG_TYPE = "type"
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
            if (cardFace.image_uris != null) cardFace.image_uris.png.toString() else null
        )
        contentValues.put(
            CARD_FACE_IMAGE_BORDER_CROP,
            if (cardFace.image_uris != null) cardFace.image_uris.border_crop.toString() else null
        )
        contentValues.put(
            CARD_FACE_IMAGE_ART_CROP,
            if (cardFace.image_uris != null) cardFace.image_uris.art_crop.toString() else null
        )
        contentValues.put(
            CARD_FACE_IMAGE_LARGE,
            if (cardFace.image_uris != null) cardFace.image_uris.large.toString() else null
        )
        contentValues.put(
            CARD_FACE_IMAGE_NORMAL,
            if (cardFace.image_uris != null) cardFace.image_uris.normal.toString() else null
        )
        contentValues.put(
            CARD_FACE_IMAGE_SMALL,
            if (cardFace.image_uris != null) cardFace.image_uris.small.toString() else null
        )
        val result = db.insert(CARD_FACE_TABLE_NAME, null, contentValues)
        return result
    }

    fun addRelatedCard(mainCardId: UUID, relatedCardId: UUID, componentType: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(RELATED_CARD_SCRYFALL_ID_MAIN, mainCardId.toString())
        contentValues.put(RELATED_CARD_SCRYFALL_ID_RELATED, relatedCardId.toString())
        contentValues.put(RELATED_CARD_COMPONENT, componentType)
        val result = db.insert(RELATED_CARD_TABLE_NAME, null, contentValues)
        return result
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
        cardArtist: String?
    ): MutableList<Card> {
        var whereStarted = false
        var query = "SELECT * FROM $CARD_TABLE_NAME"
        if (cardName != null && cardName != "") {
            query += if (whereStarted) " AND" else " WHERE"
            query += " $CARD_NAME LIKE '%${cardName}%'"
            whereStarted = true
        }
        if (!cardTypes.isNullOrEmpty() && isCardTypes != null && isCardTypes.isNotEmpty()) {
            var k = 0
            while (k < cardTypes.size) {
                query += if (!whereStarted) " WHERE (" else if (k == 0) " AND (" else if (cardTypesAnd) " AND" else " OR"
                query += " $CARD_TYPE_LINE"
                if (!isCardTypes[k]) query += " NOT"
                query += " LIKE '%${cardTypes[k]}%'"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (cardText != null && cardText != "") {
            val words = cardText.split(" ")
            var first = true
            words.forEach {
                query += if (!whereStarted) " WHERE (" else if (first) " AND (" else " AND"
                query += " $CARD_ORACLE_TEXT LIKE '%${it}%'"
                whereStarted = true
                first = false
            }
            query += ")"
        }
        if (!manaValueParamsArray.isNullOrEmpty()) {
            var k = 0
            while (k < manaValueParamsArray.size) {
                query += if (!whereStarted) " WHERE ($CARD_CMC ${manaValueParamsArray[k]}" else if (k == 0) " AND ($CARD_CMC ${manaValueParamsArray[k]}" else if (k % 2 == 0) " $CARD_CMC ${manaValueParamsArray[k]}" else " ${manaValueParamsArray[k]}"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (!powerParamsArray.isNullOrEmpty()) {
            var k = 0
            while (k < powerParamsArray.size) {
                query += if (!whereStarted) " WHERE ($CARD_POWER ${powerParamsArray[k]}" else if (k == 0) " AND ($CARD_POWER ${powerParamsArray[k]}" else if (k % 2 == 0) " $CARD_POWER ${powerParamsArray[k]}" else " ${powerParamsArray[k]}"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (!toughnessParamsArray.isNullOrEmpty()) {
            var k = 0
            while (k < toughnessParamsArray.size) {
                query += if (!whereStarted) " WHERE ($CARD_TOUGHNESS ${toughnessParamsArray[k]}" else if (k == 0) " AND ($CARD_TOUGHNESS ${toughnessParamsArray[k]}" else if (k % 2 == 0) " $CARD_TOUGHNESS ${toughnessParamsArray[k]}" else " ${toughnessParamsArray[k]}"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (!loyaltyParamsArray.isNullOrEmpty()) {
            var k = 0
            while (k < loyaltyParamsArray.size) {
                query += if (!whereStarted) " WHERE ($CARD_LOYALTY ${loyaltyParamsArray[k]}" else if (k == 0) " AND ($CARD_LOYALTY ${loyaltyParamsArray[k]}" else if (k % 2 == 0) " $CARD_LOYALTY ${loyaltyParamsArray[k]}" else " ${loyaltyParamsArray[k]}"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (!rarityParamsArray.isNullOrEmpty()) {
            var k = 0
            while (k < rarityParamsArray.size) {
                query += if (!whereStarted) " WHERE ($CARD_RARITY == '${rarityParamsArray[k].lowercase()}'" else if (k == 0) " AND ($CARD_RARITY == '${rarityParamsArray[k].lowercase()}'" else if (k % 2 == 0) " $CARD_RARITY == '${rarityParamsArray[k].lowercase()}'" else " ${rarityParamsArray[k]}"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (!legalityParamsArray.isNullOrEmpty()) {
            var k = 0
            while (k < legalityParamsArray.size) {
                query += if (!whereStarted) " WHERE (legal_${legalityParamsArray[k].split(" ")[1].lowercase()} == '${
                    legalityParamsArray[k].split(
                        " "
                    )[0].lowercase()
                }'" else if (k == 0) " AND (legal_${legalityParamsArray[k].split(" ")[1].lowercase()} == '${
                    legalityParamsArray[k].split(
                        " "
                    )[0].lowercase()
                }'" else if (k % 2 == 0) " legal_${legalityParamsArray[k].split(" ")[1].lowercase()} == '${
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
                query += if (!whereStarted) " WHERE ($CARD_LAYOUT == '${
                    layoutParamsArray[k].lowercase().replace(" ", "_")
                }'" else if (k == 0) " AND ($CARD_LAYOUT == '${
                    layoutParamsArray[k].lowercase().replace(" ", "_")
                }'" else if (k % 2 == 0) " $CARD_LAYOUT == '${
                    layoutParamsArray[k].lowercase().replace(" ", "_")
                }'" else " ${layoutParamsArray[k]}"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        else {
            query += if (!whereStarted) " WHERE (" else " AND ("
            query += "NOT $CARD_LAYOUT == 'planar' AND NOT $CARD_LAYOUT == 'scheme' AND NOT $CARD_LAYOUT == 'vanguard' AND NOT $CARD_LAYOUT == 'token' AND NOT $CARD_LAYOUT == 'double_faced_token' AND NOT $CARD_LAYOUT == 'emblem' AND NOT $CARD_LAYOUT == 'art_series' AND NOT $CARD_LAYOUT == 'reversible_card'"
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
                query += " $CARD_MANA_COST LIKE '%"
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
            if (cardColor == "C") query += if (!whereStarted) " WHERE ($CARD_COLORS == ''" else " AND ($CARD_COLORS == ''"
            else {
                when (colorOperator) {
                    "exactly" -> {
                        val colors = colorStringtoArray(cardColor)
                        val colorsNotSelected = mutableListOf("W", "U", "B", "R", "G")
                        var k = 0
                        while (k < colors.size) {
                            query += if (!whereStarted) " WHERE ($CARD_COLORS LIKE '%${colors[k]}%'" else if (k == 0) " AND ($CARD_COLORS LIKE '%${colors[k]}%'" else " AND $CARD_COLORS LIKE '%${colors[k]}%'"
                            colorsNotSelected.remove(colors[k])
                            whereStarted = true
                            ++k
                        }
                        k = 0
                        while (k < colorsNotSelected.size) {
                            query += if (k == 0) ") AND ($CARD_COLORS NOT LIKE '%${colorsNotSelected[k]}%'" else " AND $CARD_COLORS NOT LIKE '%${colorsNotSelected[k]}%'"
                            ++k
                        }
                    }
                    "including" -> {
                        val colors = colorStringtoArray(cardColor)
                        var k = 0
                        while (k < colors.size) {
                            query += if (!whereStarted) " WHERE ($CARD_COLORS LIKE '%${colors[k]}%'" else if (k == 0) " AND ($CARD_COLORS LIKE '%${colors[k]}%'" else " AND $CARD_COLORS LIKE '%${colors[k]}%'"
                            whereStarted = true
                            ++k
                        }
                    }
                    else -> {
                        val colors = colorStringtoArray(cardColor)
                        val colorsNotSelected = mutableListOf("W", "U", "B", "R", "G")
                        var k = 0
                        while (k < colors.size) {
                            query += if (!whereStarted) " WHERE ($CARD_COLORS LIKE '%${colors[k]}%'" else if (k == 0) " AND ($CARD_COLORS LIKE '%${colors[k]}%'" else " OR $CARD_COLORS LIKE '%${colors[k]}%'"
                            colorsNotSelected.remove(colors[k])
                            whereStarted = true
                            ++k
                        }
                        k = 0
                        while (k < colorsNotSelected.size) {
                            query += if (k == 0) ") AND ($CARD_COLORS NOT LIKE '%${colorsNotSelected[k]}%'" else " AND $CARD_COLORS NOT LIKE '%${colorsNotSelected[k]}%'"
                            ++k
                        }
                    }
                }
            }
            whereStarted = true
            query += ")"
        }
        if (cardColorIdentity != null && cardColorIdentity != "") {
            if (cardColorIdentity == "C") query += if (!whereStarted) " WHERE ($CARD_COLOR_IDENTITY == ''" else " AND ($CARD_COLOR_IDENTITY == ''"
            else {
                val colors = colorStringtoArray(cardColorIdentity)
                val colorsNotInIdentity = mutableListOf("W", "U", "B", "R", "G")
                var k = 0
                while (k < colors.size) {
                    query += if (!whereStarted) " WHERE ($CARD_COLOR_IDENTITY LIKE '%${colors[k]}%'" else if (k == 0) " AND ($CARD_COLOR_IDENTITY LIKE '%${colors[k]}%'" else " OR $CARD_COLOR_IDENTITY LIKE '%${colors[k]}%'"
                    colorsNotInIdentity.remove(colors[k])
                    whereStarted = true
                    ++k
                }
                k = 0
                while (k < colorsNotInIdentity.size) {
                    query += if (k == 0) ") AND ($CARD_COLOR_IDENTITY NOT LIKE '%${colorsNotInIdentity[k]}%'" else " AND $CARD_COLOR_IDENTITY NOT LIKE '%${colorsNotInIdentity[k]}%'"
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
                query += if (!whereStarted) " WHERE ($CARD_PRODUCED_MANA LIKE '%${colors[k]}%'" else if (k == 0) " AND ($CARD_PRODUCED_MANA LIKE '%${colors[k]}%'" else " AND $CARD_PRODUCED_MANA LIKE '%${colors[k]}%'"
                whereStarted = true
                ++k
            }
            query += ")"
        }
        if (cardFlavorText != null && cardFlavorText != "") {
            val words = cardFlavorText.split(" ")
            var first = true
            words.forEach {
                query += if (!whereStarted) " WHERE (" else if (first) " AND (" else " AND"
                query += " $CARD_FLAVOR_TEXT LIKE '%${it}%'"
                whereStarted = true
                first = false
            }
            query += ")"
        }
        if (priceValue != null && priceValue != "") {
            query += if (!whereStarted) " WHERE (" else " AND ("
            query += if (priceCoin == "usd") "$CARD_PRICE_USD" else if (priceCoin == "eur") "$CARD_PRICE_EUR" else "$CARD_PRICE_TIX"
            query += " $priceOperator $priceValue"
            whereStarted = true
            query += if (priceCoin == "usd") " AND $CARD_PRICE_USD NOT NULL" else if (priceCoin == "eur") " AND $CARD_PRICE_EUR NOT NULL" else " AND $CARD_PRICE_TIX NOT NULL"
            query += ")"
        }
        if (cardSet != null && cardSet != "") {
            query += if (whereStarted) " AND" else " WHERE"
            query += " $CARD_SET_SCRYFALL_ID LIKE '%${getSetID(cardSet)}%'"
            whereStarted = true
        }
        if (cardArtist != null && cardArtist != "") {
            query += if (whereStarted) " AND" else " WHERE"
            query += " $CARD_ARTIST LIKE '%${cardArtist}%'"
            whereStarted = true
        }
        query += " ORDER BY $CARD_NAME LIMIT 50"

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
                list.add(card)
            } while (result.moveToNext())
        }
        return list
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
        var query = "SELECT $CARD_NAME FROM $CARD_TABLE_NAME WHERE $CARD_NAME LIKE '%$str%' ORDER BY $CARD_NAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val name = result.getStringOrNull(result.getColumnIndex(CARD_NAME))
                if (name != null) list.add(name)
            } while (result.moveToNext())
        }
        return list
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
            val draw = getSymbolDrawable(subString)
            val proportion = draw.intrinsicWidth / draw.intrinsicHeight
            draw.setBounds(0, 0, textSize * proportion, textSize)
            val imageSpan = ImageSpan(draw, ImageSpan.ALIGN_BASELINE)
            result.setSpan(imageSpan, subStart, subEnd + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            str = str.replaceFirst('{', '0')
            str = str.replaceFirst('}', '0')
            println(str)
        }
        return result
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
            "{∞}" -> return AppCompatResources.getDrawable(
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