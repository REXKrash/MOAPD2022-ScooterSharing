package dk.itu.moapd.scootersharing

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dk.itu.moapd.scootersharing.database.*
import dk.itu.moapd.scootersharing.models.RideStatus
import dk.itu.moapd.scootersharing.models.Scooter
import dk.itu.moapd.scootersharing.utils.getOrAwaitValue
import dk.itu.moapd.scootersharing.viewmodels.ScooterDetailsViewModel
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScooterDetailsViewModelTest : TestCase() {

    private lateinit var db: AppDatabase

    private lateinit var scooterDao: ScooterDao
    private lateinit var rideDao: RideDao
    private lateinit var userDao: UserDao

    private lateinit var scooterRepository: ScooterRepository
    private lateinit var rideRepository: RideRepository
    private lateinit var userRepository: UserRepository

    private lateinit var viewModel: ScooterDetailsViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db = Room.inMemoryDatabaseBuilder(
                context, AppDatabase::class.java
            ).allowMainThreadQueries().build()
            rideDao = db.rideDao()
            scooterDao = db.scooterDao()
            userDao = db.userDao()

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

            scooterRepository = ScooterRepository(scooterDao)
            rideRepository = RideRepository(rideDao)
            userRepository = UserRepository(userDao)
            scooterRepository.insert(scooter)

            viewModel =
                ScooterDetailsViewModel(1, scooterRepository, rideRepository, userRepository)
        }
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testToggleActiveRide() {
        runBlocking {
            val currentScooter = viewModel.getScooter().getOrAwaitValue()

            Assert.assertNotNull(currentScooter)
            Assert.assertEquals(true, currentScooter?.locked)
            Assert.assertEquals(false, currentScooter?.active)

            Assert.assertNull(viewModel.getCurrentRide().getOrAwaitValue())
            Assert.assertFalse(viewModel.isRideActive())

            viewModel.toggleActiveRide()

            val currentRide = viewModel.getCurrentRide().getOrAwaitValue()

            Assert.assertNotNull(currentRide)
            Assert.assertEquals(RideStatus.RUNNING, currentRide?.status)
            Assert.assertTrue(viewModel.isRideActive())
        }
    }
}
