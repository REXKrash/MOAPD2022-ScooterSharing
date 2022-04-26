package dk.itu.moapd.scootersharing

import android.app.Application
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.models.Scooter
import dk.itu.moapd.scootersharing.viewmodels.ScooterListViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class ScooterListViewModelTest2 {

    private val scooterRepository = ScooterRepository(Mockito.mock(Application::class.java))
    private val viewModel = ScooterListViewModel(scooterRepository)

    @Test
    fun test() {
        runBlocking {
            scooterRepository.insert(
                Scooter(
                    1,
                    "Scooter 1",
                    "location",
                    randomDate(),
                    false,
                    55.0,
                    12.0
                )
            )
            Assert.assertEquals(1, viewModel.getAll().value?.size)
        }
    }

    private fun randomDate(): Long {
        val random = Random()
        val now = System.currentTimeMillis()
        val year = random.nextDouble() * 1000 * 60 * 60 * 24 * 365
        return (now - year).toLong()
    }
}
