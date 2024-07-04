package pe.turismogo.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pe.turismogo.util.Constants
import java.util.UUID

object DatabaseReferences {

    private var userReference =
        FirebaseDatabase.getInstance().getReference(Constants.DATABASE_USERS)

    private var eventReference =
        FirebaseDatabase.getInstance().getReference(Constants.DATABASE_EVENTS)

    fun getRootUsers() : DatabaseReference {
        return userReference
    }

    fun getRootEvents() : DatabaseReference {
        return eventReference
    }

    fun generatePushId() : String {
        return FirebaseDatabase.getInstance().getReference().push().getKey() ?: generateUUID()
    }

    fun generateUUID(): String {
        val randomUUID : UUID = UUID.randomUUID()
        val result = randomUUID.toString()
        Constants.INVALID_CHARACTERS.forEach {
            result.replace(it, Constants.EMPTY)
        }
        return result
    }
}