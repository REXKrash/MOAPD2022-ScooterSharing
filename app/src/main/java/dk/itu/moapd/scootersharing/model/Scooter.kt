package dk.itu.moapd.scootersharing.model

import java.text.SimpleDateFormat
import java.util.*

data class Scooter(
    val id: Int,
    var name: String,
    var where: String,
    var timestamp: Long,
    var active: Boolean = false
)

fun Scooter.getInfo(): String {
    return "$name is placed at $where, time: ${getTimestamp()}"
}

fun Scooter.getTimestamp(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    return formatter.format(calendar.time)
}
