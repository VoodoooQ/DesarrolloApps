package com.example.guaumiau.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guaumiau.data.Pet
import com.example.guaumiau.data.PetRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el estado de las mascotas
 */
class PetViewModel(
    private val repository: PetRepository = PetRepository()
) : ViewModel() {
    
    // StateFlow para observar la lista de mascotas
    val pets: StateFlow<List<Pet>> = repository.getAllPets()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // LiveData para mensajes
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    
    /**
     * Agrega una nueva mascota
     */
    fun addPet(pet: Pet) {
        viewModelScope.launch {
            try {
                repository.addPet(pet)
                _message.value = "Mascota agregada exitosamente"
            } catch (e: Exception) {
                _message.value = "Error: ${e.localizedMessage}"
            }
        }
    }
    
    /**
     * Elimina una mascota
     */
    fun deletePet(pet: Pet) {
        viewModelScope.launch {
            try {
                repository.deletePet(pet)
                _message.value = "Mascota eliminada"
            } catch (e: Exception) {
                _message.value = "Error: ${e.localizedMessage}"
            }
        }
    }
}
