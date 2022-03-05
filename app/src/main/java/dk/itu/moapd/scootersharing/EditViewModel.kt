package dk.itu.moapd.scootersharing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.itu.moapd.scootersharing.model.getInfo

class EditViewModel(private val scooterId: Int, private val ridesDB: RidesDB) : ViewModel() {

    private var infoText = MutableLiveData<String>()
    private var nameText = MutableLiveData<String>()
    private var whereText = MutableLiveData<String>()

    val infoTextState: LiveData<String>
        get() = infoText

    val nameTextState: LiveData<String>
        get() = nameText

    val whereTextState: LiveData<String>
        get() = whereText

    init {
        updateText()
    }

    fun updateScooter(name: String, where: String) {
        ridesDB.updateScooter(scooterId, name, where)
        updateText()
    }

    private fun updateText() {
        val scooter = ridesDB.getScooter(scooterId)

        infoText.value = scooter.getInfo()
        nameText.value = scooter.name
        whereText.value = scooter.where
    }
}

class EditViewModelFactory(private val scooterId: Int, private val ridesDB: RidesDB) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            return EditViewModel(scooterId, ridesDB) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
