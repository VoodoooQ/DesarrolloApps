package com.example.guaumiau.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guaumiau.MainActivity
import com.example.guaumiau.R
import com.example.guaumiau.data.local.AppDatabase
import com.example.guaumiau.data.repository.TaskRepository
import com.example.guaumiau.databinding.ActivityTaskListBinding
import com.example.guaumiau.ui.addEdit.AddEditTaskActivity
import com.example.guaumiau.viewmodels.MainViewModel
import com.example.guaumiau.viewmodels.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar

/**
 * Activity principal que muestra la lista de tareas del usuario
 */
class TaskListActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityTaskListBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var userId: String
    
    private val viewModel: MainViewModel by viewModels {
        val database = AppDatabase.getDatabase(this)
        val repository = TaskRepository(database.taskDao())
        MainViewModelFactory(repository, userId)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Obtener userId del Intent (pasado desde Login)
        userId = intent.getStringExtra(EXTRA_USER_ID) ?: run {
            // Si no hay userId, volver a login
            navigateToLogin()
            return
        }
        
        setupToolbar()
        setupRecyclerView()
        setupFab()
        observeViewModel()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Mis Tareas"
    }
    
    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onTaskClick = { task ->
                // Navegar a detalle/edición
                val intent = Intent(this, AddEditTaskActivity::class.java).apply {
                    putExtra(AddEditTaskActivity.EXTRA_USER_ID, userId)
                    putExtra(AddEditTaskActivity.EXTRA_TASK_ID, task.id)
                }
                startActivity(intent)
            },
            onTaskLongClick = { task ->
                // Mostrar opciones
                showTaskOptionsDialog(task)
            },
            onTaskStatusChange = { task ->
                // Cambiar estado de completado
                viewModel.toggleTaskStatus(task)
            },
            onDeleteClick = { task ->
                // Confirmar eliminación
                confirmDeleteTask(task)
            }
        )
        
        binding.recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(this@TaskListActivity)
            adapter = taskAdapter
        }
    }
    
    private fun setupFab() {
        binding.fabAddTask.setOnClickListener {
            val intent = Intent(this, AddEditTaskActivity::class.java).apply {
                putExtra(AddEditTaskActivity.EXTRA_USER_ID, userId)
            }
            startActivity(intent)
        }
    }
    
    private fun observeViewModel() {
        // Observar lista de tareas
        viewModel.tasks.observe(this) { tasks ->
            taskAdapter.submitList(tasks)
            updateEmptyState(tasks.isEmpty())
        }
        
        // Observar loading
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        // Observar errores
        viewModel.error.observe(this) { error ->
            error?.let {
                showSnackbar(it)
                viewModel.clearError()
            }
        }
        
        // Observar mensajes de éxito
        viewModel.successMessage.observe(this) { message ->
            message?.let {
                showSnackbar(it)
                viewModel.clearSuccessMessage()
            }
        }
    }
    
    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyStateLayout.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerViewTasks.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
    
    private fun showTaskOptionsDialog(task: com.example.guaumiau.data.model.Task) {
        val options = arrayOf("Editar", "Eliminar", "Cancelar")
        AlertDialog.Builder(this)
            .setTitle(task.title)
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        // Editar
                        val intent = Intent(this, AddEditTaskActivity::class.java).apply {
                            putExtra(AddEditTaskActivity.EXTRA_USER_ID, userId)
                            putExtra(AddEditTaskActivity.EXTRA_TASK_ID, task.id)
                        }
                        startActivity(intent)
                    }
                    1 -> {
                        // Eliminar
                        confirmDeleteTask(task)
                    }
                }
                dialog.dismiss()
            }
            .show()
    }
    
    private fun confirmDeleteTask(task: com.example.guaumiau.data.model.Task) {
        AlertDialog.Builder(this)
            .setTitle(R.string.confirm_delete_title)
            .setMessage(R.string.confirm_delete_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteTask(task)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
    
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_task_list, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                confirmLogout()
                true
            }
            R.id.action_delete_all -> {
                confirmDeleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle(R.string.logout)
            .setMessage(R.string.confirm_logout)
            .setPositiveButton(R.string.yes) { _, _ ->
                navigateToLogin()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
    
    private fun confirmDeleteAll() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar todas")
            .setMessage("¿Estás seguro de eliminar TODAS tus tareas?")
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteAllTasks()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
    
    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    
    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
    }
}
