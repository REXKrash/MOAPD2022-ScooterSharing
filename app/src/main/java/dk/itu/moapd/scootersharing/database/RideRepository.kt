package dk.itu.moapd.scootersharing.database

import android.app.Application
import androidx.lifecycle.LiveData
import dk.itu.moapd.scootersharing.models.Ride

class RideRepository(application: Application) {

    private val dao: RideDao
    private val data: LiveData<List<Ride>>

    init {
        val db = AppDatabase.getDatabase(application)
        dao = db.rideDao()
        data = dao.getAll()
    }

    suspend fun insert(ride: Ride) {
        dao.insert(ride)
    }

    suspend fun update(ride: Ride) {
        dao.update(ride)
    }

    suspend fun delete(ride: Ride) {
        dao.delete(ride)
    }

    fun getAll() = data

    fun findById(id: Int) = dao.findById(id)

    fun deleteById(id: Int) = dao.deleteById(id)

    fun getAllByUserUid(userUid: String) = dao.getAllByUserUid(userUid)
}
