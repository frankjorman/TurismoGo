package pe.turismogo.model.domain

data class Business(val id : String = "") {
    var user : User = User()
    var active : Boolean = false
}
