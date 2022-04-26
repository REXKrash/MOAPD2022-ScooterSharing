package dk.itu.moapd.scootersharing

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dk.itu.moapd.scootersharing.database.AppDatabase
import dk.itu.moapd.scootersharing.database.UserDao
import dk.itu.moapd.scootersharing.models.User
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner::class)
class UserDaoTest {

    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()
        userDao = db.userDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertUserAndFind() {
        val user = User(1, "uid", "george", "george@gmail.com")

        runBlocking {
            userDao.insert(user)
            userDao.findById(1).getOrAwaitValue()?.let {
                assertThat(it.name, equalTo(user.name))
            }
        }
    }
}
