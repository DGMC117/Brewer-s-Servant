package magicchief.main.brewersservant

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import magicchief.main.brewersservant.dataclass.Set

class DBHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Define SQL instructions
        // Create Card table
        val createCardDB = (
                "CREATE TABLE $CARD_TABLE_NAME ("
                        + CARD_ID + " char(36) primary key,"
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
                        + CARD_TYPE_LINE + "varchar(255) not null,"
                        + CARD_ARTIST + " varchar(50),"
                        + CARD_FLAVOR_TEXT + " text,"
                        + CARD_RARITY + " varchar(50) not null,"
                        + CARD_SET_ID + " char(36) not null foreign key references $CARD_SET_TABLE_NAME($CARD_SET_SET_ID),"
                        + CARD_PRICE_USD + " varchar(25) not null,"
                        + CARD_PRICE_EUR + " varchar(25) not null,"
                        + CARD_PRICE_TIX + " varchar(25) not null,"
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
                        + CARD_IMAGE_SMALL + " varchar(255)"
                        + ")"
                )
        // Create CardFace table
        val createCardFaceDB = (
                "CREATE TABLE $CARD_FACE_TABLE_NAME ("
                        + CARD_FACE_ID + " char(36) auto_increment primary key,"
                        + CARD_FACE_ID_MAIN_CARD + " char(36) not null foreign key references $CARD_TABLE_NAME($CARD_ID),"
                        + CARD_FACE_CMC + " float,"
                        + CARD_FACE_COLORS + " varchar(50),"
                        + CARD_FACE_LAYOUT + " varchar(50),"
                        + CARD_FACE_LOYALTY + " varchar(25),"
                        + CARD_FACE_MANA_COST + " varchar(100) not null,"
                        + CARD_FACE_NAME + " varchar(255) not null,"
                        + CARD_FACE_ORACLE_TEXT + " text,"
                        + CARD_FACE_POWER + " varchar(25),"
                        + CARD_FACE_TOUGHNESS + " varchar(25),"
                        + CARD_FACE_TYPE_LINE + "varchar(255),"
                        + CARD_FACE_ARTIST + " varchar(50),"
                        + CARD_FACE_FLAVOR_TEXT + " text,"
                        + CARD_FACE_IMAGE_PNG + " varchar(255),"
                        + CARD_FACE_IMAGE_BORDER_CROP + " varchar(255),"
                        + CARD_FACE_IMAGE_ART_CROP + " varchar(255),"
                        + CARD_FACE_IMAGE_LARGE + " varchar(255),"
                        + CARD_FACE_IMAGE_NORMAL + " varchar(255),"
                        + CARD_FACE_IMAGE_SMALL + " varchar(255)"
                        + ")"
                )
        // Create RelatedCard table
        val createRelatedCardDB = (
                "CREATE TABLE $RELATED_CARD_TABLE_NAME ("
                        + RELATED_CARD_ID_MAIN + " char(36) foreign key references $CARD_TABLE_NAME($CARD_ID),"
                        + RELATED_CARD_ID_RELATED + " char(36) foreign key references $CARD_TABLE_NAME($CARD_ID),"
                        + RELATED_CARD_COMPONENT + " varchar(25) not null,"
                        + "primary key ($RELATED_CARD_ID_MAIN,$RELATED_CARD_ID_RELATED)"
                        + ")"
                )
        // Create CardSet table
        val createCardSetDB = (
                "CREATE TABLE $CARD_SET_TABLE_NAME ("
                        + CARD_SET_SET_ID + " char(36) primary key,"
                        + CARD_SET_CODE + " varchar(10) not null,"
                        + CARD_SET_NAME + " varchar(50) not null,"
                        + CARD_SET_ICON_SVG_URI + " varchar(255) not null"
                        + ")"
                )

        // Execute SQL code
        db.execSQL(createCardSetDB)
        db.execSQL(createCardDB)
        db.execSQL(createCardFaceDB)
        db.execSQL(createRelatedCardDB)
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
        private val CARD_SET_ID = "set_id"
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
        private val CARD_FACE_ID = "id" // Autogenerated
        private val CARD_FACE_ID_MAIN_CARD = "id_main_card"
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
        private val RELATED_CARD_ID_MAIN = "id_main"
        private val RELATED_CARD_ID_RELATED = "id_related"
        private val RELATED_CARD_COMPONENT = "component"

        // CardSet table name
        private val CARD_SET_TABLE_NAME = "CardSet"
        // CardSet table column names
        private val CARD_SET_SET_ID = "id"
        private val CARD_SET_CODE = "code"
        private val CARD_SET_NAME = "name"
        private val CARD_SET_ICON_SVG_URI = "icon_svg_uri"

    }

    fun addCardSet (cardSet: Set): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CARD_SET_SET_ID, cardSet.id.toString())
        contentValues.put(CARD_SET_CODE, cardSet.code)
        contentValues.put(CARD_SET_NAME, cardSet.name)
        contentValues.put(CARD_SET_ICON_SVG_URI, cardSet.icon_svg_uri.toString())
        val result = db.insert(CARD_SET_TABLE_NAME, null, contentValues)
        db.close()
        return result
    }
}