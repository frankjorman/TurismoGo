package pe.turismogo.unused

import pe.turismogo.model.domain.User

@Deprecated("Deprecado")
data class Reservation(val id : String = "") {
    var businessId : String = ""
    var activityId : String = ""
    var userList : List<User> = emptyList()
    var departed : Boolean = false
    var deleted : Boolean = false
}
