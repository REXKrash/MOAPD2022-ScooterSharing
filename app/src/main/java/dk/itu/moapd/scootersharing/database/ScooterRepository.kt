package dk.itu.moapd.scootersharing.database

import androidx.lifecycle.LiveData
import dk.itu.moapd.scootersharing.models.Scooter

class ScooterRepository(private val dao: ScooterDao) {

    private val data: LiveData<List<Scooter>> = dao.getAll()

    suspend fun insert(scooter: Scooter) = dao.insert(scooter)

    suspend fun update(scooter: Scooter) = dao.update(scooter)

    suspend fun delete(scooter: Scooter) = dao.delete(scooter)

    suspend fun deleteById(id: Int) = dao.deleteById(id)

    suspend fun getById(id: Int) = dao.getById(id)

    fun getAll() = data

    fun findById(id: Int) = dao.findById(id)

    fun findByLongLat(longitude: Double, latitude: Double) =
        dao.findByLongLat(longitude, latitude)
}
