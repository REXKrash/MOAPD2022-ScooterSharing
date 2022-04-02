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

    @Query("SELECT * FROM ride")
    fun getAll(): LiveData<List<Ride>>

    @Query("SELECT * FROM ride WHERE id = :id")
    fun findById(id: Int): LiveData<Ride?>

    @Query("DELETE FROM ride WHERE id = :id")
    fun deleteById(id: Int)
}