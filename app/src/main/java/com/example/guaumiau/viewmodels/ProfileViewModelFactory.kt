package com.example.guaumiau.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.guaumiau.data.repository.PetRepository
import com.example.guaumiau.data.repository.UserRepository

/**
 * Factory para crear ProfileViewModel con dependencias
 */
class ProfileViewModelFactory(
    private val userRepository: UserRepository,
    private val petRepository: PetRepository,
    private val currentUserEmail: String
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository, petRepository, currentUserEmail) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
