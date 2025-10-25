package com.example.guaumiau.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.guaumiau.R
import com.example.guaumiau.views.AddEditPetFragment
import com.example.guaumiau.views.PetDetailFragment
import com.example.guaumiau.views.PetListFragment

/**
 * Clase para manejar la navegación entre fragmentos
 */
class NavigationManager(private val activity: FragmentActivity) {
    
    /**
     * Navega a la lista de mascotas
     */
    fun navigateToPetList() {
        replaceFragment(PetListFragment.newInstance())
    }
    
    /**
     * Navega al detalle de una mascota
     */
    fun navigateToPetDetail(petId: Int) {
        replaceFragment(PetDetailFragment.newInstance(petId), addToBackStack = true)
    }
    
    /**
     * Navega para agregar una nueva mascota
     */
    fun navigateToAddPet() {
        replaceFragment(AddEditPetFragment.newInstance(), addToBackStack = true)
    }
    
    /**
     * Navega para editar una mascota existente
     */
    fun navigateToEditPet(petId: Int) {
        replaceFragment(AddEditPetFragment.newInstance(petId), addToBackStack = true)
    }
    
    /**
     * Reemplaza el fragmento actual
     */
    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        
        transaction.commit()
    }
    
    /**
     * Vuelve al fragmento anterior
     */
    fun navigateBack(): Boolean {
        return if (activity.supportFragmentManager.backStackEntryCount > 0) {
            activity.supportFragmentManager.popBackStack()
            true
        } else {
            false
        }
    }
}
