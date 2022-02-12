package magicchief.main.brewersservant

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Define SQL instructions
        // Create Card table
        val createCardDB = (
                "CREATE TABLE $CARD_TABLE_NAME ("
                        + ")"
                )
        // Create CardFace table
        val createCardFaceDB = (
                "CREATE TABLE $CARD_FACE_TABLE_NAME ("
                        + ")"
                )
        // Create RelatedCard table
        val createRelatedCardDB = (
                "CREATE TABLE $RELATED_CARD_TABLE_NAME ("
                        + ")"
                )
        // Create CardSet table
        val createCardSetDB = (
                "CREATE TABLE $CARD_SET_TABLE_NAME ("
                        + ")"
                )

        // Execute SQL code
        db.execSQL(createCardDB)
        db.execSQL(createCardFaceDB)
        db.execSQL(createRelatedCardDB)
        db.execSQL(createCardSetDB)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // This method drops the tables if they existed and creates them again
        db.execSQL("DROP TABLE IF EXISTS $CARD_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $CARD_FACE_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $RELATED_CARD_TABLE_NAME")
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
        // CardFace table name
        private val CARD_FACE_TABLE_NAME = "CardFace"
        // RelatedCard table name
        private val RELATED_CARD_TABLE_NAME = "RelatedCard"
        // CardSet table name
        private val CARD_SET_TABLE_NAME = "CardSet"

    }
}