package dk.itu.moapd.scootersharing.database

import androidx.room.TypeConverter
import dk.itu.moapd.scootersharing.models.RideStatus

class Converters {

    @TypeConverter
    fun toRideStatus(value: String) = enumValueOf<RideStatus>(value)

    @TypeConverter
    fun fromRideStatus(value: RideStatus) = value.name
}
