package com.youtube.music.util

import android.content.Context
import android.content.SharedPreferences
import com.youtube.music.database.DatabaseHandler
import java.io.File

object SharedPref {
    var pref: SharedPreferences? = null
    var cacheDir: File? = null
    var dbHelper: DatabaseHandler? = null

    //Should be called first
    fun init(context: Context) {
        pref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        cacheDir = context.cacheDir
        dbHelper = DatabaseHandler(context)
    }

    inline fun edit(operation: (SharedPreferences.Editor) -> Unit) {
        if (pref != null) {
            val editor = pref!!.edit()
            operation(editor)
            editor.apply()
        }
    }

    fun setValue(key: String, value: Any?) {
        when (value) {
            is String -> edit { it.putString(key, value) }
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    inline fun <reified T : Any> get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> pref?.getString(key, defaultValue as? String) as T?
            Int::class -> pref?.getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> pref?.getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> pref?.getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> pref?.getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
}