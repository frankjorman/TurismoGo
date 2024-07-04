package pe.turismogo.data

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import pe.turismogo.util.Constants

object StorageReferences {
    private val storageReference = FirebaseStorage.getInstance().getReference()

    private val rootPublications = storageReference.child(Constants.STORE_PUBLICATIONS)

    fun getRootPublications() : StorageReference {
        return rootPublications
    }
}