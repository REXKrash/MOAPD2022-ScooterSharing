package dk.itu.moapd.scootersharing.database

import androidx.lifecycle.LiveData
import androidx.room.*
import dk.itu.moapd.scootersharing.models.Scooter

@Dao
interface ScooterDao {

    @Insert
    suspend fun insert(scooter: Scooter)

    @Update
    suspend fun update(scooter: Scooter)

    @Delete
    suspend fun delete(scooter: Scooter)

    @Query("SELECT * FROM scooter WHERE id = :id")
    suspend fun getById(id: Int): Scooter?

    @Query("SELECT * FROM scooter")
    fun getAll(): LiveData<List<Scooter>>

    @Query("SELECT * FROM scooter WHERE name LIKE :name")
    fun findByName(name: String): LiveData<Scooter?>

    @Query("SELECT * FROM scooter WHERE id = :id")
    fun findById(id: Int): LiveData<Scooter?>

    @Query("DELETE FROM scooter WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT * FROM scooter WHERE longitude = :longitude AND latitude = :latitude")
    fun findByLongLat(longitude: Double, latitude: Double): LiveData<Scooter?>
}
