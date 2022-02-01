package dk.itu.moapd.scootersharing.exercises

class PersonList {

    val god = Person("God", -1, null)
    val eve = Person("Eve", 5780, god)
    val adam = Person("Adam", 5780, god)
    val cain = Person("Cain", 5780, eve)
    val abel = Person("Abel", 5780, eve)
    val seth = Person("Seth", 5780, eve)
    val awan = Person("Awan", 5780, eve)
    val azura = Person("Azura", 5780, eve)
    val aclima = Person("Aclima", 5780, eve)
    val enoch = Person("Enoch", 5780, awan)
    val enos = Person("Enos", 5780, azura)

    val list = listOf(
        god,
        eve,
        adam,
        cain,
        abel,
        seth,
        awan,
        azura,
        aclima,
        enoch,
        enos
    )
}
