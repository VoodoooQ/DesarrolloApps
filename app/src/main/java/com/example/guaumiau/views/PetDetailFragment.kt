package com.example.guaumiau.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.guaumiau.R

/**
 * Fragment para mostrar los detalles de una mascota
 */
class PetDetailFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pet_detail, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // TODO: Implementar detalles de mascota
    }
    
    companion object {
        private const val ARG_PET_ID = "pet_id"
        
        fun newInstance(petId: Int) = PetDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PET_ID, petId)
            }
        }
    }
}
