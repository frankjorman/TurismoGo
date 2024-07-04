package pe.turismogo.model.domain

import pe.turismogo.util.Enums

data class UserDatabaseReferences(var id : String) {
    var role : String = Enums.Role.USER.toString()
}
