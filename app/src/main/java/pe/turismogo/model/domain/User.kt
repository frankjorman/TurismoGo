package pe.turismogo.model.domain

import pe.turismogo.util.Enums

data class User(var id : String = "") {
    var name : String = ""
    var lastname : String = ""
    var email : String = ""
    var password : String = ""
    var phone : String = ""
    var documentId : String = ""
    var legalId : String = ""
    var city : String = ""
    var role : String = Enums.Role.USER.toString()
    var businessName : String = ""
    var businessRole : String = ""
    var dateOfBirth : String = ""

    var eventsReserved : ArrayList<Event> = arrayListOf()
}
