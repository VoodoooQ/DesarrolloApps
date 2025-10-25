package com.example.guaumiau.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guaumiau.R
import com.example.guaumiau.data.Pet
import com.example.guaumiau.data.PetAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Fragment para mostrar la lista de mascotas
 */
class PetListFragment : Fragment() {
    
    private lateinit var petAdapter: PetAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddPet: FloatingActionButton
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pet_list, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        recyclerView = view.findViewById(R.id.recyclerViewPets)
        fabAddPet = view.findViewById(R.id.fabAddPet)
        
        setupRecyclerView()
        
        fabAddPet.setOnClickListener {
            Toast.makeText(requireContext(), "Agregar nueva mascota", Toast.LENGTH_SHORT).show()
        }
        
        loadSampleData()
    }
    
    private fun setupRecyclerView() {
        petAdapter = PetAdapter { pet ->
            Toast.makeText(requireContext(), "Seleccionaste: ${pet.name}", Toast.LENGTH_SHORT).show()
        }
        
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = petAdapter
        }
    }
    
    private fun loadSampleData() {
        val samplePets = listOf(
            Pet(1, "Firulais", "perro", "Labrador", 3, null, "Un perro amigable"),
            Pet(2, "Michi", "gato", "Persa", 2, null, "Gato cariñoso"),
            Pet(3, "Max", "perro", "Golden Retriever", 5, null, "Perro leal"),
            Pet(4, "Luna", "gato", "Siamés", 1, null, "Gatita juguetona")
        )
        petAdapter.submitList(samplePets)
    }
    
    companion object {
        fun newInstance() = PetListFragment()
    }
}
