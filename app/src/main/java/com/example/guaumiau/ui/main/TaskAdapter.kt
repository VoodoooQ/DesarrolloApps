package com.example.guaumiau.ui.main

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.guaumiau.data.model.Task
import com.example.guaumiau.databinding.ItemTaskBinding

/**
 * Adapter para mostrar la lista de tareas en RecyclerView
 */
class TaskAdapter(
    private val onTaskClick: (Task) -> Unit,
    private val onTaskLongClick: (Task) -> Unit,
    private val onTaskStatusChange: (Task) -> Unit,
    private val onDeleteClick: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class TaskViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(task: Task) {
            binding.apply {
                // Título
                tvTaskTitle.text = task.title
                
                // Descripción
                tvTaskDescription.text = task.description
                
                // Fecha
                tvTaskDate.text = task.getFormattedDate()
                
                // Estado de completado
                checkboxCompleted.isChecked = task.isCompleted
                
                // Aplicar estilo de tachado si está completado
                if (task.isCompleted) {
                    tvTaskTitle.paintFlags = tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvTaskDescription.paintFlags = tvTaskDescription.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    tvTaskTitle.paintFlags = tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    tvTaskDescription.paintFlags = tvTaskDescription.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
                
                // Click en el item
                root.setOnClickListener {
                    onTaskClick(task)
                }
                
                // Long click en el item
                root.setOnLongClickListener {
                    onTaskLongClick(task)
                    true
                }
                
                // Checkbox change
                checkboxCompleted.setOnCheckedChangeListener { _, _ ->
                    // Solo ejecutar si el checkbox fue clicado por el usuario
                    if (checkboxCompleted.isPressed) {
                        onTaskStatusChange(task)
                    }
                }
                
                // Botón eliminar
                btnDelete.setOnClickListener {
                    onDeleteClick(task)
                }
            }
        }
    }
    
    /**
     * DiffUtil para optimizar las actualizaciones del RecyclerView
     */
    private class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}
