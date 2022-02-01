package dk.itu.moapd.scootersharing.model

data class Scooter(
    var name: String,
    var where: String
)

fun Scooter.getInfo(): String {
    return " $name is placed at $where "
}
