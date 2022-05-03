package dk.itu.moapd.scootersharing.database

import androidx.lifecycle.LiveData
import dk.itu.moapd.scootersharing.models.Ride

class RideRepository(private val dao: RideDao) {

    private val data: LiveData<List<Ride>> = dao.getAll()

    suspend fun insert(ride: Ride) = dao.insert(ride)

    suspend fun update(ride: Ride) = dao.update(ride)

    suspend fun delete(ride: Ride) = dao.delete(ride)

    suspend fun deleteById(id: Int) = dao.deleteById(id)

    suspend fun getByRideUid(rideUid: String) = dao.getByRideUid(rideUid)

    fun getAll() = data

    fun findById(id: Int) = dao.findById(id)

    fun getAllByUserUid(userUid: String) = dao.getAllByUserUid(userUid)
}
