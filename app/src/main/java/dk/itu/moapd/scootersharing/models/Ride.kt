package dk.itu.moapd.scootersharing.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ride")
class Ride(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "scooterId")
    var scooterId: Int,
    @ColumnInfo(name = "status")
    var status: RideStatus,
    @ColumnInfo(name = "rentalTime")
    var rentalTime: Long,
    @ColumnInfo(name = "initialLocation")
    var initialLocation: String,
    @ColumnInfo(name = "currentLocation")
    var currentLocation: String,
    @ColumnInfo(name = "price")
    var price: Double,
    @ColumnInfo(name = "userUid")
    var userUid: String
)
