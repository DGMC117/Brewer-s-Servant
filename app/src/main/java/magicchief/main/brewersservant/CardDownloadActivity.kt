package magicchief.main.brewersservant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import com.google.gson.GsonBuilder
import magicchief.main.brewersservant.dataclass.Card
import magicchief.main.brewersservant.dataclass.ScryfallBulkData
import magicchief.main.brewersservant.dataclass.Set
import magicchief.main.brewersservant.dataclass.SetList
import okhttp3.*
import okhttp3.internal.wait
import java.io.IOException
import java.util.*

class CardDownloadActivity : AppCompatActivity() {

    val scryfallBulkDataEndpoint = "https://api.scryfall.com/bulk-data"
    val scryfallSetsEndpoint = "https://api.scryfall.com/sets"
    val dataCategory = "oracle_cards"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_download)
        setDownload()
        cardDownload()
    }

    private fun setDownload () {
        val gson = GsonBuilder().create()
        val request = Request.Builder().url(scryfallSetsEndpoint).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    println("ERROR downloading file")
                    TODO("Handle error")
                }
                Looper.prepare()
                Toast.makeText(applicationContext, "Downloading set data...", Toast.LENGTH_SHORT).show()

                val body = response.body?.string()
                val setList = gson.fromJson(body, SetList::class.java)
                val dbHelper = DBHelper(applicationContext)
                setList.data.forEach {
                    dbHelper.addCardSet(it)
                    println(it.name)
                }
                Toast.makeText(applicationContext, "Sets download complete!", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun cardDownload () {
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
                        val dbHelper = DBHelper(applicationContext)
                        val inStr = response.body?.byteStream()
                        val reader = gson.newJsonReader(inStr?.reader())
                        reader.beginArray()
                        while (reader.hasNext()) {
                            val currentCard: Card = gson.fromJson(reader, Card::class.java)
                            dbHelper.addCard(currentCard)
                            if (!currentCard.all_parts.isNullOrEmpty()) currentCard.all_parts.forEach { dbHelper.addRelatedCard(currentCard.id, it.id, it.component) }
                            if (!currentCard.card_faces.isNullOrEmpty()) currentCard.card_faces.forEach { dbHelper.addCardFace(it, currentCard.id) }
                            println(currentCard.name)
                        }
                        reader.endArray()
                        reader.close()
                        Toast.makeText(applicationContext, "Cards download complete!", Toast.LENGTH_SHORT).show()
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
}