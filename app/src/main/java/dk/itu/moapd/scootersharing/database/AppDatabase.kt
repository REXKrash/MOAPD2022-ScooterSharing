package dk.itu.moapd.scootersharing.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dk.itu.moapd.scootersharing.models.Scooter

@Database(entities = [Scooter::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scooterDao(): ScooterDao
    abstract fun userDao(): UserDao
    abstract fun rideDao(): RideDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database_name"
                ).build()

                INSTANCE = instance

                // return instance
                instance
            }
        }
    }
}
