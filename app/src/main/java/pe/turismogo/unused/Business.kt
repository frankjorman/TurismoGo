package pe.turismogo.unused

import pe.turismogo.model.domain.User

@Deprecated("No Usado")
data class Business(val id : String = "") {
    var user : User = User()
    var active : Boolean = false
}
