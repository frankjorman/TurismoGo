package pe.turismogo.model.domain

data class Review(var id : String = "") {
    var userId : String = ""
    var userName : String = ""
    var comment : String = ""
    var rating : Int = 0
}
