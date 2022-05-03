package dk.itu.moapd.scootersharing.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "uid")
    val uid: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "email")
    var email: String,
    @ColumnInfo(name = "balance")
    var balance: Double = 200.0,
)
