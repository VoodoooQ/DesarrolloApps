package com.example.guaumiau.ui.addEdit

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.guaumiau.R
import com.example.guaumiau.data.local.AppDatabase
import com.example.guaumiau.data.repository.TaskRepository
import com.example.guaumiau.databinding.ActivityAddEditTaskBinding
import com.example.guaumiau.viewmodels.AddEditViewModel
import com.example.guaumiau.viewmodels.AddEditViewModelFactory
import com.google.android.material.snackbar.Snackbar

/**
 * Activity para crear o editar una tarea
 */
class AddEditTaskActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAddEditTaskBinding
    private lateinit var userId: String
    private var taskId: Int? = null
    
    private val viewModel: AddEditViewModel by viewModels {
        val database = AppDatabase.getDatabase(this)
        val repository = TaskRepository(database.taskDao())
        AddEditViewModelFactory(repository, userId, taskId)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Obtener datos del Intent
        userId = intent.getStringExtra(EXTRA_USER_ID) ?: run {
            finish()
            return
        }
        
        taskId = intent.getIntExtra(EXTRA_TASK_ID, -1).let {
            if (it == -1) null else it
        }
        
        setupToolbar()
        setupViews()
        observeViewModel()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = if (viewModel.isEditMode()) "Editar Tarea" else "Nueva Tarea"
        }
        
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun setupViews() {
        // Listeners para los campos de texto
        binding.etTitle.doAfterTextChanged { text ->
            viewModel.title.value = text?.toString() ?: ""
            viewModel.clearTitleError()
        }
        
        binding.etDescription.doAfterTextChanged { text ->
            viewModel.description.value = text?.toString() ?: ""
            viewModel.clearDescriptionError()
        }
        
        // Botón guardar
        binding.btnSave.setOnClickListener {
            viewModel.saveTask()
        }
    }
    
    private fun observeViewModel() {
        // Observar la tarea actual (si estamos editando)
        viewModel.currentTask.observe(this) { task ->
            task?.let {
                binding.etTitle.setText(it.title)
                binding.etDescription.setText(it.description)
            }
        }
        
        // Observar campos del formulario
        viewModel.title.observe(this) { /* Ya se maneja en el doAfterTextChanged */ }
        viewModel.description.observe(this) { /* Ya se maneja en el doAfterTextChanged */ }
        
        // Observar errores de validación
        viewModel.titleError.observe(this) { error ->
            binding.tilTitle.error = error
        }
        
        viewModel.descriptionError.observe(this) { error ->
            binding.tilDescription.error = error
        }
        
        // Observar loading
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSave.isEnabled = !isLoading
        }
        
        // Observar errores generales
        viewModel.error.observe(this) { error ->
            error?.let {
                showSnackbar(it)
                viewModel.clearError()
            }
        }
        
        // Observar guardado exitoso
        viewModel.taskSaved.observe(this) { saved ->
            if (saved) {
                val message = if (viewModel.isEditMode()) {
                    getString(R.string.task_updated)
                } else {
                    getString(R.string.task_saved)
                }
                showSnackbar(message)
                // Cerrar activity después de un breve delay
                binding.root.postDelayed({
                    finish()
                }, 500)
            }
        }
    }
    
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
    
    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
        const val EXTRA_TASK_ID = "extra_task_id"
    }
}
