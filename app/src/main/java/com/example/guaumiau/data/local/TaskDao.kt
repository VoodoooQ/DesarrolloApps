package com.example.guaumiau.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.guaumiau.data.model.Task

/**
 * DAO (Data Access Object) para operaciones CRUD de Task
 */
@Dao
interface TaskDao {
    
    /**
     * Obtener todas las tareas de un usuario específico
     */
    @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY date DESC")
    fun getTasksByUser(userId: String): LiveData<List<Task>>
    
    /**
     * Obtener una tarea por su ID
     */
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?
    
    /**
     * Obtener tareas completadas de un usuario
     */
    @Query("SELECT * FROM tasks WHERE userId = :userId AND isCompleted = 1 ORDER BY date DESC")
    fun getCompletedTasks(userId: String): LiveData<List<Task>>
    
    /**
     * Obtener tareas pendientes de un usuario
     */
    @Query("SELECT * FROM tasks WHERE userId = :userId AND isCompleted = 0 ORDER BY date DESC")
    fun getPendingTasks(userId: String): LiveData<List<Task>>
    
    /**
     * Insertar una nueva tarea
     * @return El ID de la tarea insertada
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long
    
    /**
     * Actualizar una tarea existente
     */
    @Update
    suspend fun updateTask(task: Task)
    
    /**
     * Eliminar una tarea específica
     */
    @Delete
    suspend fun deleteTask(task: Task)
    
    /**
     * Eliminar todas las tareas de un usuario
     */
    @Query("DELETE FROM tasks WHERE userId = :userId")
    suspend fun deleteAllTasksByUser(userId: String)
    
    /**
     * Marcar tarea como completada/pendiente
     */
    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: Int, isCompleted: Boolean)
    
    /**
     * Contar tareas pendientes de un usuario
     */
    @Query("SELECT COUNT(*) FROM tasks WHERE userId = :userId AND isCompleted = 0")
    fun getPendingTasksCount(userId: String): LiveData<Int>
}
