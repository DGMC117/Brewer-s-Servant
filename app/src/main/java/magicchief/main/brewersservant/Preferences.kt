package magicchief.main.brewersservant

import android.content.Context
import android.content.SharedPreferences

class Preferences (context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("AppControlData", Context.MODE_PRIVATE)

    private var DATABASE_DOWNLOAD_DONE = "databaseDownloadDone"

    var databaseDownloadDone: Boolean
        get() = preferences.getBoolean(DATABASE_DOWNLOAD_DONE, false)
        set(value) = preferences.edit().putBoolean(DATABASE_DOWNLOAD_DONE, value).apply()
}