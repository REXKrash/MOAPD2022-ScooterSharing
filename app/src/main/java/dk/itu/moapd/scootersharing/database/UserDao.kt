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

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE user SET balance = balance - :amount WHERE uid LIKE :uid")
    suspend fun decreaseBalance(uid: String, amount: Double)

    @Query("SELECT * FROM user WHERE uid LIKE :uid")
    suspend fun getByUid(uid: String): User?

    @Query("SELECT * FROM user WHERE uid LIKE :uid")
    fun findByUid(uid: String): LiveData<User?>

    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE id = :id")
    fun findById(id: Int): LiveData<User?>
}
