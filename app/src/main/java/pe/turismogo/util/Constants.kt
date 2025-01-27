package pe.turismogo.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object Constants {
    const val TAG_COMMON = "Turismo GO"

    const val ADMIN_TEMPORAL_USER = "admin"
    const val ADMIN_TEMPORAL_PASSWORD = "1234"
    const val USER_TEMPORAL_USER = "user"
    const val USER_TEMPORAL_PASSWORD = "1234"

    const val SPACE = " "
    const val DOT = "."
    const val SLASH = "/"
    const val BWD_SLASH = "\\"
    const val UNDERSCORE = "_"
    const val SEMI_COLON = ","
    const val COLON = ";"
    const val EQUAL = "="
    const val DASH = "-"
    const val AT = "@"
    const val EMPTY = ""

    val INVALID_CHARACTERS = arrayOf(
        SPACE,
        DOT,
        SLASH,
        BWD_SLASH,
        UNDERSCORE,
        SEMI_COLON,
        COLON,
        EQUAL,
        DASH
    )

    const val ONE_SEC : Long = 1000L

    const val DATABASE_USERS = "users"
    const val DATABASE_EVENTS = "events"
    const val DATABASE_PARTICIPANTS = "participants"
    const val DATABASE_REVIEWS = "reviews"

    const val STORE_IMAGES = "images"
    const val STORE_PUBLICATIONS = "publications"

    const val DIRECTORY_LOCAL_IMAGES: String = "image/*"

    const val CACHE_PREF = "cache"
    const val CACHE_EVENT = "event"



}