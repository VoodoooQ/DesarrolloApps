package com.example.guaumiau.viewmodels

import androidx.lifecycle.*
import com.example.guaumiau.data.model.Task
import com.example.guaumiau.data.repository.TaskRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla principal (lista de tareas)
 */
class MainViewModel(
    private val repository: TaskRepository,
    private val userId: String
) : ViewModel() {
    
    // Estados de la UI
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> = _successMessage
    
    // Datos
    val tasks: LiveData<List<Task>> = repository.getTasksByUser(userId)
    
    val pendingTasksCount: LiveData<Int> = repository.getPendingTasksCount(userId)
    
    /**
     * Elimina una tarea
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteTask(task)
                _successMessage.value = "Tarea eliminada correctamente"
            } catch (e: Exception) {
                _error.value = "Error al eliminar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Cambia el estado de completado de una tarea
     */
    fun toggleTaskStatus(task: Task) {
        viewModelScope.launch {
            try {
                repository.toggleTaskStatus(task.id, !task.isCompleted)
                val status = if (!task.isCompleted) "completada" else "pendiente"
                _successMessage.value = "Tarea marcada como $status"
            } catch (e: Exception) {
                _error.value = "Error al actualizar: ${e.message}"
            }
        }
    }
    
    /**
     * Elimina todas las tareas del usuario
     */
    fun deleteAllTasks() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteAllTasksByUser(userId)
                _successMessage.value = "Todas las tareas eliminadas"
            } catch (e: Exception) {
                _error.value = "Error al eliminar todas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _error.value = null
    }
    
    /**
     * Limpia el mensaje de éxito
     */
    fun clearSuccessMessage() {
        _successMessage.value = null
    }
}

/**
 * Factory para crear MainViewModel con parámetros
 */
class MainViewModelFactory(
    private val repository: TaskRepository,
    private val userId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
