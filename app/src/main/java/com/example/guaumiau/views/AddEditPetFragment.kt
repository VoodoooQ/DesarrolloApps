package com.example.guaumiau.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.guaumiau.R

/**
 * Fragment para agregar o editar una mascota
 */
class AddEditPetFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_pet, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // TODO: Implementar formulario de agregar/editar mascota
    }
    
    companion object {
        private const val ARG_PET_ID = "pet_id"
        
        fun newInstance(petId: Int? = null) = AddEditPetFragment().apply {
            arguments = Bundle().apply {
                petId?.let { putInt(ARG_PET_ID, it) }
            }
        }
    }
}
