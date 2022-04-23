package dk.itu.moapd.scootersharing.database

import android.app.Application
import androidx.lifecycle.LiveData
import dk.itu.moapd.scootersharing.models.Scooter

class ScooterRepository(application: Application) {

    private val dao: ScooterDao
    private val data: LiveData<List<Scooter>>

    init {
        val db = AppDatabase.getDatabase(application)
        dao = db.scooterDao()
        data = dao.getAll()
    }

    suspend fun insert(scooter: Scooter) {
        dao.insert(scooter)
    }

    suspend fun update(scooter: Scooter) {
        dao.update(scooter)
    }

    suspend fun delete(scooter: Scooter) {
        dao.delete(scooter)
    }

    fun getAll() = data

    fun findByName(name: String) = dao.findByName(name)

    fun findById(id: Int) = dao.findById(id)

    fun deleteById(id: Int) = dao.deleteById(id)

    fun findByLongLat(longitude: Double, latitude: Double) = dao.findByLongLat(longitude, latitude)
}
