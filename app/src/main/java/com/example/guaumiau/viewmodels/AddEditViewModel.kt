package com.example.guaumiau.viewmodels

import androidx.lifecycle.*
import com.example.guaumiau.data.model.Task
import com.example.guaumiau.data.repository.TaskRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para crear y editar tareas
 */
class AddEditViewModel(
    private val repository: TaskRepository,
    private val userId: String,
    private val taskId: Int? = null
) : ViewModel() {
    
    // Estados de la UI
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _taskSaved = MutableLiveData<Boolean>()
    val taskSaved: LiveData<Boolean> = _taskSaved
    
    // Datos de la tarea actual (si estamos editando)
    private val _currentTask = MutableLiveData<Task?>()
    val currentTask: LiveData<Task?> = _currentTask
    
    // Campos del formulario
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    
    // Validaciones
    private val _titleError = MutableLiveData<String?>()
    val titleError: LiveData<String?> = _titleError
    
    private val _descriptionError = MutableLiveData<String?>()
    val descriptionError: LiveData<String?> = _descriptionError
    
    init {
        // Si tenemos taskId, cargar la tarea para editar
        taskId?.let { loadTask(it) }
    }
    
    /**
     * Carga una tarea existente para editar
     */
    private fun loadTask(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val task = repository.getTaskById(id)
                _currentTask.value = task
                
                // Cargar datos en los campos
                task?.let {
                    title.value = it.title
                    description.value = it.description
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar tarea: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Valida el título
     */
    private fun validateTitle(): Boolean {
        val titleValue = title.value?.trim() ?: ""
        return when {
            titleValue.isEmpty() -> {
                _titleError.value = "El título es obligatorio"
                false
            }
            titleValue.length < 3 -> {
                _titleError.value = "El título debe tener al menos 3 caracteres"
                false
            }
            titleValue.length > 100 -> {
                _titleError.value = "El título no puede exceder 100 caracteres"
                false
            }
            else -> {
                _titleError.value = null
                true
            }
        }
    }
    
    /**
     * Valida la descripción
     */
    private fun validateDescription(): Boolean {
        val descValue = description.value?.trim() ?: ""
        return when {
            descValue.isEmpty() -> {
                _descriptionError.value = "La descripción es obligatoria"
                false
            }
            descValue.length < 5 -> {
                _descriptionError.value = "La descripción debe tener al menos 5 caracteres"
                false
            }
            descValue.length > 500 -> {
                _descriptionError.value = "La descripción no puede exceder 500 caracteres"
                false
            }
            else -> {
                _descriptionError.value = null
                true
            }
        }
    }
    
    /**
     * Limpia errores de validación
     */
    fun clearTitleError() {
        _titleError.value = null
    }
    
    fun clearDescriptionError() {
        _descriptionError.value = null
    }
    
    /**
     * Guarda o actualiza la tarea
     */
    fun saveTask() {
        // Validar campos
        val isTitleValid = validateTitle()
        val isDescValid = validateDescription()
        
        if (!isTitleValid || !isDescValid) {
            return
        }
        
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                val task = if (taskId != null) {
                    // Actualizar tarea existente
                    _currentTask.value?.copy(
                        title = title.value!!.trim(),
                        description = description.value!!.trim()
                    ) ?: return@launch
                } else {
                    // Crear nueva tarea
                    Task(
                        title = title.value!!.trim(),
                        description = description.value!!.trim(),
                        userId = userId,
                        date = System.currentTimeMillis()
                    )
                }
                
                if (taskId != null) {
                    repository.updateTask(task)
                } else {
                    repository.insertTask(task)
                }
                
                _taskSaved.value = true
            } catch (e: Exception) {
                _error.value = "Error al guardar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Verifica si estamos en modo edición
     */
    fun isEditMode(): Boolean = taskId != null
    
    /**
     * Limpia mensajes de error
     */
    fun clearError() {
        _error.value = null
    }
}

/**
 * Factory para crear AddEditViewModel con parámetros
 */
class AddEditViewModelFactory(
    private val repository: TaskRepository,
    private val userId: String,
    private val taskId: Int? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddEditViewModel(repository, userId, taskId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
