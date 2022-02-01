package dk.itu.moapd.scootersharing

import dk.itu.moapd.scootersharing.exercises.PersonList
import dk.itu.moapd.scootersharing.exercises.getDescendantsOf
import dk.itu.moapd.scootersharing.exercises.getNonDescendantsOf
import dk.itu.moapd.scootersharing.exercises.isChildOf
import org.junit.Assert.*
import org.junit.Test

class Exercise2Test {

    private val persons = PersonList()

    @Test
    fun getDescendantsOf_correctDescendants() {
        assertEquals(8, persons.eve.getDescendantsOf(persons.list).size)
    }

    @Test
    fun getNonDescendantsOf_correctNonDescendants() {
        assertEquals(3, persons.eve.getNonDescendantsOf(persons.list).size)
    }

    @Test
    fun isChildOf() {
        assertTrue(persons.cain.isChildOf(persons.eve))
        assertFalse(persons.god.isChildOf(persons.eve))
    }
}
