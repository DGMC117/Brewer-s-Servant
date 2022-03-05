package magicchief.main.brewersservant.dataclass

import java.net.URI

class ScryfallBulkDataObject (
    val id: String,
    val type: String,
    val updated_at: String,
    val uri: URI,
    val name: String,
    val description: String,
    val compressed_size: Int,
    val download_uri: URI,
    val content_type: String,
    val content_encoding: String
        )