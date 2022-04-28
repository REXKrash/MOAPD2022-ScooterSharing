package dk.itu.moapd.scootersharing

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dk.itu.moapd.scootersharing.database.AppDatabase
import dk.itu.moapd.scootersharing.database.ScooterDao
import dk.itu.moapd.scootersharing.models.Scooter
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
class ScooterDaoTest {

    private lateinit var scooterDao: ScooterDao
    private lateinit var db: AppDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()
        scooterDao = db.scooterDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertScooterAndFindById() {
        val scooter = Scooter(
            id = 1,
            name = "name",
            location = "location",
            timestamp = 0L,
            active = false,
            locked = true,
            latitude = 55.0,
            longitude = 12.0,
            imageUri = "",
            batteryLevel = 20.0
        )
        runBlocking {
            scooterDao.insert(scooter)
            scooterDao.findById(1).getOrAwaitValue()?.let {
                assertThat(it.name, equalTo(scooter.name))
                assertThat(it.location, equalTo(scooter.location))
                assertThat(it.timestamp, equalTo(scooter.timestamp))
                assertThat(it.active, equalTo(scooter.active))
                assertThat(it.locked, equalTo(scooter.locked))
                assertThat(it.latitude, equalTo(scooter.latitude))
                assertThat(it.longitude, equalTo(scooter.longitude))
                assertThat(it.imageUri, equalTo(scooter.imageUri))
                assertThat(it.batteryLevel, equalTo(scooter.batteryLevel))
            }
        }
    }

    @Test
    fun insertScooterAndGetAll() {
        val scooter = Scooter(
            id = 1,
            name = "name",
            location = "location",
            timestamp = 0L,
            active = false,
            locked = true,
            latitude = 55.0,
            longitude = 12.0,
            imageUri = "",
            batteryLevel = 20.0
        )
        runBlocking {
            scooterDao.insert(scooter)
            scooterDao.getAll().getOrAwaitValue().let {
                val first = it.first()
                assertThat(first.name, equalTo(scooter.name))
                assertThat(first.location, equalTo(scooter.location))
                assertThat(first.timestamp, equalTo(scooter.timestamp))
                assertThat(first.active, equalTo(scooter.active))
                assertThat(first.locked, equalTo(scooter.locked))
                assertThat(first.latitude, equalTo(scooter.latitude))
                assertThat(first.longitude, equalTo(scooter.longitude))
                assertThat(first.imageUri, equalTo(scooter.imageUri))
                assertThat(first.batteryLevel, equalTo(scooter.batteryLevel))
            }
        }
    }

    @Test
    fun insertScooterThenDelete() {
        val scooter = Scooter(
            id = 1,
            name = "name",
            location = "location",
            timestamp = 0L,
            active = false,
            locked = true,
            latitude = 55.0,
            longitude = 12.0,
            imageUri = "",
            batteryLevel = 20.0
        )
        runBlocking {
            assertThat(scooterDao.getAll().getOrAwaitValue().size, equalTo(0))
            scooterDao.insert(scooter)
            assertThat(scooterDao.getAll().getOrAwaitValue().size, equalTo(1))
            scooterDao.delete(scooter)
            assertThat(scooterDao.getAll().getOrAwaitValue().size, equalTo(0))
        }
    }

    @Test
    fun insertScooterThenDeleteById() {
        val scooter = Scooter(
            id = 1,
            name = "name",
            location = "location",
            timestamp = 0L,
            active = false,
            locked = true,
            latitude = 55.0,
            longitude = 12.0,
            imageUri = "",
            batteryLevel = 20.0
        )
        runBlocking {
            assertThat(scooterDao.getAll().getOrAwaitValue().size, equalTo(0))
            scooterDao.insert(scooter)
            assertThat(scooterDao.getAll().getOrAwaitValue().size, equalTo(1))
            scooterDao.deleteById(1)
            assertThat(scooterDao.getAll().getOrAwaitValue().size, equalTo(0))
        }
    }

    @Test
    fun insertScooterAndUpdate() {
        val scooter = Scooter(
            id = 1,
            name = "name",
            location = "location",
            timestamp = 0L,
            active = false,
            locked = true,
            latitude = 55.0,
            longitude = 12.0,
            imageUri = "",
            batteryLevel = 20.0
        )
        runBlocking {
            scooterDao.insert(scooter)
            scooterDao.findById(1).getOrAwaitValue()?.let {
                assertThat(it.name, equalTo(scooter.name))
                assertThat(it.location, equalTo(scooter.location))
                assertThat(it.timestamp, equalTo(scooter.timestamp))
                assertThat(it.active, equalTo(scooter.active))
                assertThat(it.locked, equalTo(scooter.locked))
                assertThat(it.latitude, equalTo(scooter.latitude))
                assertThat(it.longitude, equalTo(scooter.longitude))
                assertThat(it.imageUri, equalTo(scooter.imageUri))
                assertThat(it.batteryLevel, equalTo(scooter.batteryLevel))
            }
            scooter.name = "super scooter"
            scooterDao.update(scooter)
            scooterDao.findById(1).getOrAwaitValue()?.let {
                assertThat(it.name, equalTo(scooter.name))
                assertThat(it.location, equalTo(scooter.location))
                assertThat(it.timestamp, equalTo(scooter.timestamp))
                assertThat(it.active, equalTo(scooter.active))
                assertThat(it.locked, equalTo(scooter.locked))
                assertThat(it.latitude, equalTo(scooter.latitude))
                assertThat(it.longitude, equalTo(scooter.longitude))
                assertThat(it.imageUri, equalTo(scooter.imageUri))
                assertThat(it.batteryLevel, equalTo(scooter.batteryLevel))
            }
        }
    }
}
