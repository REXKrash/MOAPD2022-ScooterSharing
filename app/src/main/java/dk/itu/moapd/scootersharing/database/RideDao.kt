package dk.itu.moapd.scootersharing.database

import androidx.lifecycle.LiveData
import androidx.room.*
import dk.itu.moapd.scootersharing.models.Ride

@Dao
interface RideDao {

    @Insert
    suspend fun insert(ride: Ride)

    @Update
    suspend fun update(ride: Ride)

    @Delete
    suspend fun delete(ride: Ride)

    @Query("DELETE FROM ride WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM ride WHERE rideUid = :rideUid")
    suspend fun getByRideUid(rideUid: String): Ride?

    @Query("SELECT * FROM ride WHERE scooterId = :scooterId AND status = :status")
    fun getRideByScooterIdAndStatus(scooterId: Int, status: String): LiveData<Ride?>

    @Query("SELECT * FROM ride")
    fun getAll(): LiveData<List<Ride>>

    @Query("SELECT * FROM ride WHERE id = :id")
    fun findById(id: Int): LiveData<Ride?>

    @Query("SELECT * FROM ride WHERE userUid = :userUid")
    fun getAllByUserUid(userUid: String): LiveData<List<Ride>>
}
