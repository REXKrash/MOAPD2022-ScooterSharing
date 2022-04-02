package dk.itu.moapd.scootersharing.database

import androidx.lifecycle.LiveData
import androidx.room.*
import dk.itu.moapd.scootersharing.models.User

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE name LIKE :name")
    fun findByName(name: String): LiveData<User?>

    @Query("SELECT * FROM user WHERE uid = :uid")
    fun findByUid(uid: String): LiveData<User?>

    @Query("DELETE FROM user WHERE uid = :uid")
    fun deleteByUid(uid: String)
}
