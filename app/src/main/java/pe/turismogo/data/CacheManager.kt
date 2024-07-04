package pe.turismogo.data

import android.app.Activity
import android.content.Context
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import pe.turismogo.model.domain.Event
import pe.turismogo.util.Constants

class CacheManager {

    companion object {
        private var instance: CacheManager? = null
        fun getInstance(): CacheManager {
            if (instance == null) {
                instance = CacheManager()
            }
            return instance as CacheManager
        }

        fun destroyInstance() {
            instance = null
        }
    }

    fun saveEventCache(activity: Activity, event: Event) {
        val sharedPreferences =
            activity.getSharedPreferences(Constants.CACHE_PREF, Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        val type = object : TypeToken<Event>() {}.type
        val gson = Gson()
        val json = gson.toJson(event)
        editor.putString(Constants.CACHE_EVENT, json)
        editor.apply()
    }


    fun getEventCache(activity: Activity): Event {
        val sharedPreferences =
            activity.getSharedPreferences(Constants.CACHE_PREF, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(Constants.CACHE_EVENT, null)
        return if (json == null)
            Event()
        else
            gson.fromJson(json, Event::class.java)
    }
}