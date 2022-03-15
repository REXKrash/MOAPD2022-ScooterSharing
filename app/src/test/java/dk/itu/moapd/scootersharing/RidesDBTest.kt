package dk.itu.moapd.scootersharing

import android.content.Context
import dk.itu.moapd.scootersharing.utils.RidesDB
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock

class RidesDBTest {

    private val ridesDB = RidesDB.get(mock(Context::class.java))

    @Test
    fun getScooters_correctSize_addScooter_correctSize() {
        Assert.assertEquals(3, ridesDB.getScooters().size)
        ridesDB.addScooter("SuperScooter", "Airport")
        Assert.assertEquals(4, ridesDB.getScooters().size)
        Assert.assertTrue(ridesDB.getLastScooterInfo().contains("SuperScooter is placed at Airport, time"))
    }

    @Test
    fun addScooter_thenDeleteScooter_correctSize() {
        Assert.assertEquals(3, ridesDB.getScooters().size)
        ridesDB.addScooter("SuperScooter 2", "Bus stop")
        Assert.assertEquals(4, ridesDB.getScooters().size)
        ridesDB.deleteScooter(3)
        Assert.assertEquals(3, ridesDB.getScooters().size)
    }

    @Test
    fun updateScooter_whereCorrectlyChanged() {
        var scooter = ridesDB.getScooter(2)
        Assert.assertEquals("Rambo", scooter.name)
        Assert.assertEquals("Kobenhavns Lufthavn", scooter.where)

        ridesDB.updateScooter(2, "Rambobo", "ITU")
        scooter = ridesDB.getScooter(2)

        Assert.assertEquals("Rambobo", scooter.name)
        Assert.assertEquals("ITU", scooter.where)
    }
}
