package com.example.guaumiau.data.repository

import androidx.lifecycle.LiveData
import com.example.guaumiau.data.local.TaskDao
import com.example.guaumiau.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository para gestionar operaciones de Task
 * Abstrae la fuente de datos (Room) de los ViewModels
 */
class TaskRepository(private val taskDao: TaskDao) {
    
    /**
     * Obtiene todas las tareas de un usuario (LiveData)
     */
    fun getTasksByUser(userId: String): LiveData<List<Task>> {
        return taskDao.getTasksByUser(userId)
    }
    
    /**
     * Obtiene tareas completadas
     */
    fun getCompletedTasks(userId: String): LiveData<List<Task>> {
        return taskDao.getCompletedTasks(userId)
    }
    
    /**
     * Obtiene tareas pendientes
     */
    fun getPendingTasks(userId: String): LiveData<List<Task>> {
        return taskDao.getPendingTasks(userId)
    }
    
    /**
     * Obtiene el contador de tareas pendientes
     */
    fun getPendingTasksCount(userId: String): LiveData<Int> {
        return taskDao.getPendingTasksCount(userId)
    }
    
    /**
     * Obtiene una tarea específica por ID
     */
    suspend fun getTaskById(taskId: Int): Task? {
        return withContext(Dispatchers.IO) {
            taskDao.getTaskById(taskId)
        }
    }
    
    /**
     * Inserta una nueva tarea
     * @return El ID de la tarea insertada
     */
    suspend fun insertTask(task: Task): Long {
        return withContext(Dispatchers.IO) {
            taskDao.insertTask(task)
        }
    }
    
    /**
     * Actualiza una tarea existente
     */
    suspend fun updateTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.updateTask(task)
        }
    }
    
    /**
     * Elimina una tarea
     */
    suspend fun deleteTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.deleteTask(task)
        }
    }
    
    /**
     * Elimina todas las tareas de un usuario
     */
    suspend fun deleteAllTasksByUser(userId: String) {
        withContext(Dispatchers.IO) {
            taskDao.deleteAllTasksByUser(userId)
        }
    }
    
    /**
     * Cambia el estado de completado de una tarea
     */
    suspend fun toggleTaskStatus(taskId: Int, isCompleted: Boolean) {
        withContext(Dispatchers.IO) {
            taskDao.updateTaskStatus(taskId, isCompleted)
        }
    }
}
