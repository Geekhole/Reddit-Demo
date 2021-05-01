package uk.geekhole.hotreddit.global

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceManager @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        const val PREFERENCE_KEY = "uk.geekhole.hotreddit.global.system_preferences"
        const val KEY_PAGING_REFERENCE = "paging_after"
    }

    private var prefs: SharedPreferences? = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)

    operator fun get(key: String): String? {
        return prefs?.getString(key, null)
    }

    operator fun set(key: String, value: String?) {
        val editor = prefs?.edit() ?: return
        if (value != null) {
            editor.putString(key, value)
        } else {
            editor.remove(key)
        }

        editor.apply()
    }
}