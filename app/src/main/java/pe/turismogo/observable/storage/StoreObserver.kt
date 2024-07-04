package pe.turismogo.observable.storage

interface StoreObserver {

    interface UploadImage {
        fun notifyUploadImageObservers(isSuccessful : Boolean, url : String)
    }

    interface DeleteImage {
        fun notifyDeleteImageObservers(isSuccessful : Boolean)
    }
}