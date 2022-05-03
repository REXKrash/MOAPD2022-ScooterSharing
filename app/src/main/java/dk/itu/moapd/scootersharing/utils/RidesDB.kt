package dk.itu.moapd.scootersharing.utils

import android.content.Context
import dk.itu.moapd.scootersharing.models.Scooter
import dk.itu.moapd.scootersharing.models.getInfo
import java.util.*

class RidesDB private constructor(context: Context) {
    private val rides = ArrayList<Scooter>()

    companion object : RidesDBHolder<RidesDB, Context>(::RidesDB)

    init {
        rides.add(
            Scooter(
                id = 0,
                name = "Chuck Norris",
                location = "ITU",
                timestamp = randomDate(),
                active = false,
                locked = true,
                latitude = 0.0,
                longitude = 0.0
            )
        )
        rides.add(
            Scooter(
                id = 1,
                name = "Bruce Lee",
                location = "Fields",
                timestamp = randomDate(),
                active = false,
                locked = true,
                latitude = 0.0,
                longitude = 0.0
            )
        )
        rides.add(
            Scooter(
                id = 2,
                name = "Rambo",
                location = "Kobenhavns Lufthavn",
                timestamp = randomDate(),
                active = false,
                locked = true,
                latitude = 0.0,
                longitude = 0.0
            )
        )
    }

    fun getScooters(): List<Scooter> {
        return rides
    }

    fun getScooter(id: Int): Scooter {
        return rides.filter { scooter -> scooter.id == id }[0]
    }

    fun addScooter(name: String, location: String) {
        rides.add(
            Scooter(
                id = rides.size,
                name = name,
                location = location,
                timestamp = randomDate(),
                active = false,
                locked = true,
                latitude = 0.0,
                longitude = 0.0
            )
        )
    }

    fun deleteScooter(id: Int): Boolean {
        if (rides.any { scooter -> scooter.id == id }) {
            rides.removeAll(rides.filter { scooter -> scooter.id == id })
            return true
        }
        return false
    }

    fun updateScooter(id: Int, name: String, location: String) {
        rides.filter { scooter -> scooter.id == id }
            .forEach { scooter ->
                scooter.apply {
                    this.name = name
                    this.location = location
                }
            }
    }

    fun getLastScooterInfo(): String {
        return rides[rides.size - 1].getInfo()
    }

    private fun randomDate(): Long {
        val random = Random()
        val now = System.currentTimeMillis()
        val year = random.nextDouble() * 1000 * 60 * 60 * 24 * 365
        return (now - year).toLong()
    }
}

open class RidesDBHolder<out T : Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun get(arg: A): T {
        val checkInstance = instance
        if (checkInstance != null)
            return checkInstance

        return synchronized(this) {
            val checkInstanceAgain = instance
            if (checkInstanceAgain != null)
                checkInstanceAgain
            else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}
