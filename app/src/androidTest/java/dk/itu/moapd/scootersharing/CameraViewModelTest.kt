package dk.itu.moapd.scootersharing

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dk.itu.moapd.scootersharing.database.AppDatabase
import dk.itu.moapd.scootersharing.database.ScooterDao
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.models.Scooter
import dk.itu.moapd.scootersharing.utils.getOrAwaitValue
import dk.itu.moapd.scootersharing.viewmodels.CameraViewModel
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CameraViewModelTest : TestCase() {

    private lateinit var db: AppDatabase
    private lateinit var scooterDao: ScooterDao
    private lateinit var scooterRepository: ScooterRepository

    private lateinit var viewModel: CameraViewModel

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

            val scooter = Scooter(
                id = 1,
                name = "Scooter 1",
                location = "location",
                timestamp = 0L,
                active = false,
                locked = true,
                latitude = 55.629526888870515,
                longitude = 12.579146202544704,
                imageUri = "uriString",
                batteryLevel = 20.0
            )

            scooterRepository = ScooterRepository(scooterDao)
            scooterRepository.insert(scooter)

            viewModel = CameraViewModel(1, scooterRepository)
        }
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testUpdateScooterImage() {
        viewModel.updateScooterImage(Uri.parse("uriString"))

        val scooter = scooterRepository.findById(1).getOrAwaitValue()
        Assert.assertNotNull(scooter)
        Assert.assertEquals("uriString", scooter?.imageUri)
    }
}
