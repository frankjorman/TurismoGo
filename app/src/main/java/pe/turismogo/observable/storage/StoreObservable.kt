package pe.turismogo.observable.storage

interface StoreObservable {

    interface UploadImage {
        fun addUploadImageObserver(observer : StoreObserver.UploadImage)
        fun removeUploadImageObserver(observer : StoreObserver.UploadImage)
        fun notifyUploadImageObservers(isSuccessful : Boolean, url : String)
    }

    interface DeleteImage {
        fun addDeleteImageObserver(observer : StoreObserver.DeleteImage)
        fun removeDeleteImageObserver(observer : StoreObserver.DeleteImage)
        fun notifyDeleteImageObservers(isSuccessful : Boolean)
    }
}