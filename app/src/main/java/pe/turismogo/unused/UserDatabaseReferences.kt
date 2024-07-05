package pe.turismogo.unused

import pe.turismogo.util.Enums

@Deprecated("Deprecado")
data class UserDatabaseReferences(var id : String) {
    var role : String = Enums.Role.USER.toString()
}
