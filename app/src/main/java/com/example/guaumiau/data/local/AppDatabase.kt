package com.example.guaumiau.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.guaumiau.data.model.Task
import com.example.guaumiau.data.model.UserEntity

/**
 * Base de datos principal de la aplicación usando Room
 * Singleton pattern para garantizar una única instancia
 */
@Database(
    entities = [Task::class, UserEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    /**
     * Proporciona acceso al DAO de Task
     */
    abstract fun taskDao(): TaskDao
    
    /**
     * Proporciona acceso al DAO de User
     */
    abstract fun userDao(): UserDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        /**
         * Obtiene la instancia única de la base de datos
         * Thread-safe usando sincronización doble
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "guaumiau_database"
                )
                    .fallbackToDestructiveMigration() // En producción, usar migraciones apropiadas
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        /**
         * Destruye la instancia (útil para testing)
         */
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
