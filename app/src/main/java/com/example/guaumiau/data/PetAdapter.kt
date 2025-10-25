package com.example.guaumiau.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.guaumiau.R

/**
 * Adaptador para la lista de mascotas
 */
class PetAdapter(
    private val onPetClick: (Pet) -> Unit
) : ListAdapter<Pet, PetAdapter.PetViewHolder>(PetDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(view, onPetClick)
    }
    
    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class PetViewHolder(
        itemView: View,
        private val onPetClick: (Pet) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val tvPetName: TextView = itemView.findViewById(R.id.tvPetName)
        private val tvPetBreed: TextView = itemView.findViewById(R.id.tvPetBreed)
        private val tvPetAge: TextView = itemView.findViewById(R.id.tvPetAge)
        
        fun bind(pet: Pet) {
            tvPetName.text = pet.name
            tvPetBreed.text = pet.breed
            tvPetAge.text = "${pet.age} años"
            
            itemView.setOnClickListener {
                onPetClick(pet)
            }
        }
    }
    
    private class PetDiffCallback : DiffUtil.ItemCallback<Pet>() {
        override fun areItemsTheSame(oldItem: Pet, newItem: Pet): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Pet, newItem: Pet): Boolean {
            return oldItem == newItem
        }
    }
}
