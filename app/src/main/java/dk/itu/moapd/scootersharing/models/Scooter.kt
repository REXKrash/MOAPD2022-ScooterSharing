package dk.itu.moapd.scootersharing.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "scooter")
class Scooter(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "where")
    var where: String,
    @ColumnInfo(name = "timestamp")
    var timestamp: Long,
    @ColumnInfo(name = "active")
    var active: Boolean = false,
    @ColumnInfo(name = "latitude")
    var latitude: Double,
    @ColumnInfo(name = "longitude")
    var longitude: Double,
    @ColumnInfo(name = "imageUri")
    var imageUri: String = "",
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
