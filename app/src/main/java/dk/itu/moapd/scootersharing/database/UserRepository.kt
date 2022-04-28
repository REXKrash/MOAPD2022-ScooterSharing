package dk.itu.moapd.scootersharing.database

import androidx.lifecycle.LiveData
import dk.itu.moapd.scootersharing.models.User

class UserRepository(private val dao: UserDao) {

    private val data: LiveData<List<User>> = dao.getAll()

    suspend fun insert(user: User) {
        dao.insert(user)
    }

    suspend fun update(user: User) {
        dao.update(user)
    }

    suspend fun delete(user: User) {
        dao.delete(user)
    }

    fun getAll() = data

    fun findByUid(uid: String) = dao.findByUid(uid)

    fun findById(id: Int) = dao.findById(id)

    fun deleteById(id: Int) = dao.deleteById(id)
}
