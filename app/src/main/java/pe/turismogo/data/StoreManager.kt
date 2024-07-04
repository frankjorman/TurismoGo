package pe.turismogo.data

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import pe.turismogo.observable.storage.StoreObservable
import pe.turismogo.observable.storage.StoreObserver
import pe.turismogo.util.Constants

open class StoreManager :
    StoreObservable.UploadImage ,
    StoreObservable.DeleteImage {

    private val uploadImageObserverList : ArrayList<StoreObserver.UploadImage> = arrayListOf()
    private val deleteImageObserverList : ArrayList<StoreObserver.DeleteImage> = arrayListOf()

    private val publicationStorageReference = StorageReferences.getRootPublications()

    private val uploadTask = OnCompleteListener<Uri> { p0 ->
        notifyUploadImageObservers(p0.isSuccessful, p0.result.toString())
    }

    private val uploadImageListener =
        OnCompleteListener<UploadTask.TaskSnapshot> { p0 ->
            p0.result.storage.downloadUrl.addOnCompleteListener(uploadTask)
        }

    private val deleteImageListener =
        OnCompleteListener<Void> { p0 ->
            notifyDeleteImageObservers(p0.isSuccessful)
        }

    fun uploadImage(idPublication : String, url : String) {

        val uri = Uri.parse(url)

        publicationStorageReference
            .child(idPublication)
            .putFile(uri)
            .addOnCompleteListener(uploadImageListener)
    }

    fun deleteImage(idPublication: String) {
        val reference = FirebaseStorage.getInstance().getReferenceFromUrl(idPublication)
        reference
            .delete()
            .addOnCompleteListener(deleteImageListener)
    }


    override fun addUploadImageObserver(observer: StoreObserver.UploadImage) {
        uploadImageObserverList.add(observer)
    }

    override fun removeUploadImageObserver(observer: StoreObserver.UploadImage) {
        uploadImageObserverList.remove(observer)
    }

    override fun notifyUploadImageObservers(isSuccessful: Boolean, url : String) {
        uploadImageObserverList.forEach {
            it.notifyUploadImageObservers(isSuccessful, url)
        }
    }

    override fun addDeleteImageObserver(observer: StoreObserver.DeleteImage) {
        deleteImageObserverList.add(observer)
    }

    override fun removeDeleteImageObserver(observer: StoreObserver.DeleteImage) {
        deleteImageObserverList.remove(observer)
    }

    override fun notifyDeleteImageObservers(isSuccessful: Boolean) {
        Log.d(Constants.TAG_COMMON, "store manager: delete image ${isSuccessful}")
        deleteImageObserverList.forEach {
            it.notifyDeleteImageObservers(isSuccessful)
        }
    }

}