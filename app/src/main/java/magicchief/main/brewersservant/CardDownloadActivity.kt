package magicchief.main.brewersservant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import com.google.gson.GsonBuilder
import magicchief.main.brewersservant.dataclass.*
import magicchief.main.brewersservant.dataclass.Set
import okhttp3.*
import okhttp3.internal.wait
import java.io.IOException
import java.util.*

class CardDownloadActivity : AppCompatActivity() {

    val scryfallBulkDataEndpoint = "https://api.scryfall.com/bulk-data"
    val scryfallSetsEndpoint = "https://api.scryfall.com/sets"
    val dataCategory = "oracle_cards"
    val artistCatalogEndpoint = "https://api.scryfall.com/catalog/artist-names"
    val creatureTypeCatalogEndpoint = "https://api.scryfall.com/catalog/creature-types"
    val planeswalkerTypeCatalogEndpoint = "https://api.scryfall.com/catalog/planeswalker-types"
    val landTypeCatalogEndpoint = "https://api.scryfall.com/catalog/land-types"
    val artifactTypeCatalogEndpoint = "https://api.scryfall.com/catalog/artifact-types"
    val enchantmentTypeCatalogEndpoint = "https://api.scryfall.com/catalog/enchantment-types"
    val spellTypeCatalogEndpoint = "https://api.scryfall.com/catalog/spell-types"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_download)
        val dbHelper = DBHelper (applicationContext)
        artistCatalogDownload (dbHelper)
        creatureTypeCatalogDownload (dbHelper)
        planeswalkerTypeCatalogDownload (dbHelper)
        landTypeCatalogDownload (dbHelper)
        artifactTypeCatalogDownload (dbHelper)
        enchantmentTypeCatalogDownload (dbHelper)
        spellTypeCatalogDownload (dbHelper)
        setDownload(dbHelper)
        cardDownload(dbHelper)
    }

    private fun setDownload (dbHelper: DBHelper) {
        val gson = GsonBuilder().create()
        val request = Request.Builder().url(scryfallSetsEndpoint).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    println("ERROR downloading file")
                    TODO("Handle error")
                }

                val body = response.body?.string()
                val setList = gson.fromJson(body, SetList::class.java)
                setList.data.forEach {
                    dbHelper.addCardSet(it)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun cardDownload (dbHelper: DBHelper) {
        val gson = GsonBuilder().create()
        val request = Request.Builder().url(scryfallBulkDataEndpoint).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                lateinit var bulkDataUrl: String
                val body = response.body?.string()
                val bulkDataInfo = gson.fromJson(body, ScryfallBulkData::class.java)
                bulkDataInfo.data.forEach { if (it.type == dataCategory) bulkDataUrl = it.download_uri.toString() }
                val requestBulkData = Request.Builder().url(bulkDataUrl).build()
                client.newCall(requestBulkData).enqueue(object: Callback {
                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful) {
                            println("ERROR downloading file")
                            TODO("Handle error")
                        }
                        Looper.prepare()
                        Toast.makeText(applicationContext, "Downloading card data...", Toast.LENGTH_SHORT).show()
                        val inStr = response.body?.byteStream()
                        val reader = gson.newJsonReader(inStr?.reader())
                        reader.beginArray()
                        while (reader.hasNext()) {
                            val currentCard: Card = gson.fromJson(reader, Card::class.java)
                            dbHelper.addCard(currentCard)
                            if (!currentCard.all_parts.isNullOrEmpty()) currentCard.all_parts.forEach { dbHelper.addRelatedCard(currentCard.id!!, it.id, it.component) }
                            if (!currentCard.card_faces.isNullOrEmpty()) currentCard.card_faces!!.forEach { dbHelper.addCardFace(it, currentCard.id!!) }
                        }
                        reader.endArray()
                        reader.close()
                        Toast.makeText(applicationContext, "Cards download complete!", Toast.LENGTH_SHORT).show()
                        val sharedPref = Preferences(applicationContext)
                        sharedPref.databaseDownloadDone = true
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        TODO("Not yet implemented")
                    }
                })
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun artistCatalogDownload (dbHelper: DBHelper) {
        val gson = GsonBuilder().create()
        val request = Request.Builder().url(artistCatalogEndpoint).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val artistNames = gson.fromJson(body, Catalog::class.java)
                artistNames.data.forEach { dbHelper.addArtistCatalog(it) }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun creatureTypeCatalogDownload (dbHelper: DBHelper) {
        val gson = GsonBuilder().create()
        val request = Request.Builder().url(creatureTypeCatalogEndpoint).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val types = gson.fromJson(body, Catalog::class.java)
                types.data.forEach { dbHelper.addCreatureTypeCatalog(it) }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun planeswalkerTypeCatalogDownload (dbHelper: DBHelper) {
        val gson = GsonBuilder().create()
        val request = Request.Builder().url(planeswalkerTypeCatalogEndpoint).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val types = gson.fromJson(body, Catalog::class.java)
                types.data.forEach { dbHelper.addPlaneswalkerTypeCatalog(it) }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun landTypeCatalogDownload (dbHelper: DBHelper) {
        val gson = GsonBuilder().create()
        val request = Request.Builder().url(landTypeCatalogEndpoint).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val types = gson.fromJson(body, Catalog::class.java)
                types.data.forEach { dbHelper.addLandTypeCatalog(it) }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun artifactTypeCatalogDownload (dbHelper: DBHelper) {
        val gson = GsonBuilder().create()
        val request = Request.Builder().url(artifactTypeCatalogEndpoint).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val types = gson.fromJson(body, Catalog::class.java)
                types.data.forEach { dbHelper.addArtifactTypeCatalog(it) }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun enchantmentTypeCatalogDownload (dbHelper: DBHelper) {
        val gson = GsonBuilder().create()
        val request = Request.Builder().url(enchantmentTypeCatalogEndpoint).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val types = gson.fromJson(body, Catalog::class.java)
                types.data.forEach { dbHelper.addEnchantmentTypeCatalog(it) }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun spellTypeCatalogDownload (dbHelper: DBHelper) {
        val gson = GsonBuilder().create()
        val request = Request.Builder().url(spellTypeCatalogEndpoint).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val types = gson.fromJson(body, Catalog::class.java)
                types.data.forEach { dbHelper.addSpellTypeCatalog(it) }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }
}