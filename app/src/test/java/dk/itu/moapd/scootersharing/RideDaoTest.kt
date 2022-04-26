package dk.itu.moapd.scootersharing

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dk.itu.moapd.scootersharing.database.AppDatabase
import dk.itu.moapd.scootersharing.database.RideDao
import dk.itu.moapd.scootersharing.models.Ride
import dk.itu.moapd.scootersharing.models.RideStatus
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
class RideDaoTest {

    private lateinit var rideDao: RideDao
    private lateinit var db: AppDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()
        rideDao = db.rideDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertRideAndFindById() {
        val ride = Ride(
            1,
            1,
            RideStatus.FINISHED,
            0,
            "initial",
            "current",
            1.0,
            "userUid"
        )

        runBlocking {
            rideDao.insert(ride)
            rideDao.findById(1).getOrAwaitValue()?.let {
                assertThat(it.scooterId, equalTo(ride.scooterId))
                assertThat(it.status, equalTo(ride.status))
                assertThat(it.rentalTime, equalTo(ride.rentalTime))
                assertThat(it.initialLocation, equalTo(ride.initialLocation))
                assertThat(it.currentLocation, equalTo(ride.currentLocation))
                assertThat(it.price, equalTo(ride.price))
                assertThat(it.userUid, equalTo(ride.userUid))
            }
        }
    }

    @Test
    fun insertRideAndGetAll() {
        val ride = Ride(
            1,
            1,
            RideStatus.FINISHED,
            0,
            "initial",
            "current",
            1.0,
            "userUid"
        )

        runBlocking {
            rideDao.insert(ride)
            rideDao.getAll().getOrAwaitValue().let {
                val first = it.first()
                assertThat(first.scooterId, equalTo(ride.scooterId))
                assertThat(first.status, equalTo(ride.status))
                assertThat(first.rentalTime, equalTo(ride.rentalTime))
                assertThat(first.initialLocation, equalTo(ride.initialLocation))
                assertThat(first.currentLocation, equalTo(ride.currentLocation))
                assertThat(first.price, equalTo(ride.price))
                assertThat(first.userUid, equalTo(ride.userUid))
            }
        }
    }

    @Test
    fun insertRideThenDelete() {
        val ride = Ride(
            1,
            1,
            RideStatus.FINISHED,
            0,
            "initial",
            "current",
            1.0,
            "userUid"
        )

        runBlocking {
            assertThat(rideDao.getAll().getOrAwaitValue().size, equalTo(0))
            rideDao.insert(ride)
            assertThat(rideDao.getAll().getOrAwaitValue().size, equalTo(1))
            rideDao.delete(ride)
            assertThat(rideDao.getAll().getOrAwaitValue().size, equalTo(0))
        }
    }

    @Test
    fun insertRideThenDeleteById() {
        val ride = Ride(
            1,
            1,
            RideStatus.FINISHED,
            0,
            "initial",
            "current",
            1.0,
            "userUid"
        )

        runBlocking {
            assertThat(rideDao.getAll().getOrAwaitValue().size, equalTo(0))
            rideDao.insert(ride)
            assertThat(rideDao.getAll().getOrAwaitValue().size, equalTo(1))
            rideDao.deleteById(1)
            assertThat(rideDao.getAll().getOrAwaitValue().size, equalTo(0))
        }
    }

    @Test
    fun insertRideAndUpdate() {
        val ride = Ride(
            1,
            1,
            RideStatus.FINISHED,
            0,
            "initial",
            "current",
            1.0,
            "userUid"
        )

        runBlocking {
            rideDao.insert(ride)
            rideDao.findById(1).getOrAwaitValue()?.let {
                assertThat(it.scooterId, equalTo(ride.scooterId))
                assertThat(it.status, equalTo(ride.status))
                assertThat(it.rentalTime, equalTo(ride.rentalTime))
                assertThat(it.initialLocation, equalTo(ride.initialLocation))
                assertThat(it.currentLocation, equalTo(ride.currentLocation))
                assertThat(it.price, equalTo(ride.price))
                assertThat(it.userUid, equalTo(ride.userUid))
            }
            ride.initialLocation = "new initial location"
            rideDao.update(ride)
            rideDao.findById(1).getOrAwaitValue()?.let {
                assertThat(it.scooterId, equalTo(ride.scooterId))
                assertThat(it.status, equalTo(ride.status))
                assertThat(it.rentalTime, equalTo(ride.rentalTime))
                assertThat(it.initialLocation, equalTo(ride.initialLocation))
                assertThat(it.currentLocation, equalTo(ride.currentLocation))
                assertThat(it.price, equalTo(ride.price))
                assertThat(it.userUid, equalTo(ride.userUid))
            }
        }
    }
}
