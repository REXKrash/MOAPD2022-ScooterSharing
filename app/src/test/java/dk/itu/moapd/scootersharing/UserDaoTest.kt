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
    fun insertUserAndFindById() {
        val user = User(1, "uid", "george", "george@gmail.com")

        runBlocking {
            userDao.insert(user)
            userDao.findById(1).getOrAwaitValue()?.let {
                assertThat(it.name, equalTo(user.name))
                assertThat(it.uid, equalTo(user.uid))
                assertThat(it.email, equalTo(user.email))
            }
        }
    }

    @Test
    fun insertUserAndFindByUid() {
        val user = User(1, "uid", "george", "george@gmail.com")

        runBlocking {
            userDao.insert(user)
            userDao.findByUid("uid").getOrAwaitValue()?.let {
                assertThat(it.name, equalTo(user.name))
                assertThat(it.uid, equalTo(user.uid))
                assertThat(it.email, equalTo(user.email))
            }
        }
    }

    @Test
    fun insertUserAndGetAll() {
        val user = User(1, "uid", "george", "george@gmail.com")

        runBlocking {
            userDao.insert(user)
            userDao.getAll().getOrAwaitValue().let {
                val first = it.first()
                assertThat(first.name, equalTo(user.name))
                assertThat(first.uid, equalTo(user.uid))
                assertThat(first.email, equalTo(user.email))
            }
        }
    }

    @Test
    fun insertUserThenDelete() {
        val user = User(1, "uid", "george", "george@gmail.com")

        runBlocking {
            assertThat(userDao.getAll().getOrAwaitValue().size, equalTo(0))
            userDao.insert(user)
            assertThat(userDao.getAll().getOrAwaitValue().size, equalTo(1))
            userDao.delete(user)
            assertThat(userDao.getAll().getOrAwaitValue().size, equalTo(0))
        }
    }

    @Test
    fun insertUserThenDeleteById() {
        val user = User(1, "uid", "george", "george@gmail.com")

        runBlocking {
            assertThat(userDao.getAll().getOrAwaitValue().size, equalTo(0))
            userDao.insert(user)
            assertThat(userDao.getAll().getOrAwaitValue().size, equalTo(1))
            userDao.deleteById(1)
            assertThat(userDao.getAll().getOrAwaitValue().size, equalTo(0))
        }
    }

    @Test
    fun insertUserAndUpdate() {
        val user = User(1, "uid", "george", "george@gmail.com")

        runBlocking {
            userDao.insert(user)
            userDao.findById(1).getOrAwaitValue()?.let {
                assertThat(it.name, equalTo(user.name))
                assertThat(it.uid, equalTo(user.uid))
                assertThat(it.email, equalTo(user.email))
            }
            user.name = "jack"
            userDao.update(user)
            userDao.findById(1).getOrAwaitValue()?.let {
                assertThat(it.name, equalTo(user.name))
                assertThat(it.uid, equalTo(user.uid))
                assertThat(it.email, equalTo(user.email))
            }
        }
    }
}
