package dk.itu.moapd.scootersharing

import android.content.Context
import dk.itu.moapd.scootersharing.model.Scooter
import dk.itu.moapd.scootersharing.model.getInfo
import java.util.*

class RidesDB private constructor(context: Context) {
    private val rides = ArrayList<Scooter>()

    companion object : RidesDBHolder<RidesDB, Context>(::RidesDB)

    init {
        rides.add(
            Scooter(
                0,
                "Chuck Norris",
                "ITU", randomDate(),
                false
            )
        )
        rides.add(
            Scooter(
                1,
                "Bruce Lee",
                "Fields", randomDate(),
                false
            )
        )
        rides.add(
            Scooter(
                2,
                "Rambo",
                "Kobenhavns Lufthavn",
                randomDate(),
                false
            )
        )
    }

    fun getScooters(): List<Scooter> {
        return rides
    }

    fun getScooter(id: Int): Scooter {
        return rides.filter { scooter -> scooter.id == id }[0]
    }

    fun addScooter(name: String, where: String) {
        rides.add(Scooter(rides.size, name, where, randomDate()))
    }

    fun deleteScooter(id: Int): Boolean {
        if (rides.any { scooter -> scooter.id == id }) {
            rides.removeAll(rides.filter { scooter -> scooter.id == id })
            return true
        }
        return false
    }

    fun toggleActive(id: Int) {
        rides.filter { scooter -> scooter.id == id }
            .forEach { scooter ->
                scooter.apply {
                    this.active = !this.active
                }
            }
    }

    fun updateScooter(id: Int, name: String, where: String) {
        rides.filter { scooter -> scooter.id == id }
            .forEach { scooter ->
                scooter.apply {
                    this.name = name
                    this.where = where
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
