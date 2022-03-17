package dk.itu.moapd.scootersharing.database

import android.app.Application
import androidx.lifecycle.LiveData
import dk.itu.moapd.scootersharing.models.Scooter

class ScooterRepository(application: Application) {

    private val scooterDao: ScooterDao

    private val data: LiveData<List<Scooter>>

    init {
        val db = AppDatabase.getDatabase(application)
        scooterDao = db.scooterDao()
        data = scooterDao.getAll()
    }

    suspend fun insert(scooter: Scooter) {
        scooterDao.insert(scooter)
    }

    suspend fun update(scooter: Scooter) {
        scooterDao.update(scooter)
    }

    suspend fun delete(scooter: Scooter) {
        scooterDao.delete(scooter)
    }

    fun getAll() = data

    fun findByName(name: String) = scooterDao.findByName(name)

    fun findById(id: Int) = scooterDao.findById(id)

    fun deleteById(id: Int) = scooterDao.deleteById(id)
}
