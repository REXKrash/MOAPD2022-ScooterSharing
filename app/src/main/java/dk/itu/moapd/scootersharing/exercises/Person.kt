package dk.itu.moapd.scootersharing.exercises

data class Person(
    val name: String,
    val age: Int,
    val mother: Person?
)

fun Person.isChildOf(parent: Person): Boolean {
    return this.mother?.equals(parent) == true
}

fun Person.getDescendantsOf(fromPersons: List<Person>): List<Person> {
    val descendants = mutableListOf<Person>()

    descendants.addAll(fromPersons.filter { person -> person.mother?.equals(this) == true })
    descendants.addAll(fromPersons.filter { person -> person.mother?.mother?.equals(this) == true })

    return descendants
}

fun Person.getNonDescendantsOf(fromPersons: List<Person>): List<Person> {
    val nonDescendants = mutableListOf<Person>()

    nonDescendants.addAll(fromPersons)
    nonDescendants.removeAll(this.getDescendantsOf(fromPersons))

    return nonDescendants
}
