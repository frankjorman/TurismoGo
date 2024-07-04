package pe.turismogo.model.domain

import java.io.Serializable


data class Event(var id : String = "") : Serializable {
    var image : String = ""

    var title : String = ""
    var description : String = ""

//    var date : String = ""

    var departureDate : String = ""
    var returnDate : String = ""

    var cost : String = ""
    var itinerary : String = ""

//    var location : String = ""

    var meetingPoint : String = ""
    var destinationPoint : String = ""

    var availableSeats = 0

    var active : Boolean = true

    var owner : User = User()

    var participants : ArrayList<User>  = arrayListOf()
    var reviews : ArrayList<Review> = arrayListOf()
}
