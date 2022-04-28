package dk.itu.moapd.scootersharing

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dk.itu.moapd.scootersharing.database.AppDatabase
import dk.itu.moapd.scootersharing.database.ScooterDao
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.models.Scooter
import dk.itu.moapd.scootersharing.viewmodels.ScannerViewModel
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScannerViewModelTest : TestCase() {

    private lateinit var db: AppDatabase
    private lateinit var scooterDao: ScooterDao
    private lateinit var scooterRepository: ScooterRepository

    private lateinit var viewModel: ScannerViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db = Room.inMemoryDatabaseBuilder(
                context, AppDatabase::class.java
            ).allowMainThreadQueries().build()
            scooterDao = db.scooterDao()

            val scooter1 = Scooter(
                id = 1,
                name = "Scooter 1",
                location = "location",
                timestamp = 0L,
                active = false,
                locked = true,
                latitude = 55.629526888870515,
                longitude = 12.579146202544704,
                imageUri = "",
                batteryLevel = 20.0
            )
            val scooter2 = Scooter(
                id = 2,
                name = "Scooter 2",
                location = "location",
                timestamp = 0L,
                active = false,
                locked = true,
                latitude = 55.63787677915786,
                longitude = 12.582772549132839,
                imageUri = "",
                batteryLevel = 20.0
            )

            scooterRepository = ScooterRepository(scooterDao)
            scooterRepository.insert(scooter1)
            scooterRepository.insert(scooter2)

            viewModel = ScannerViewModel(scooterRepository)
        }
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testParseScooter() {
        viewModel.parseScooter("https://maps.google.com/local?q=55.629526888870515,12.579146202544704")

        val scooter1 = viewModel.getScooter().getOrAwaitValue()
        Assert.assertNotNull(scooter1)
        Assert.assertEquals("Scooter 1", scooter1?.name)

        viewModel.parseScooter("https://maps.google.com/local?q=55.63787677915786,12.582772549132839")

        val scooter2 = viewModel.getScooter().getOrAwaitValue()
        Assert.assertNotNull(scooter2)
        Assert.assertEquals("Scooter 2", scooter2?.name)
    }
}
