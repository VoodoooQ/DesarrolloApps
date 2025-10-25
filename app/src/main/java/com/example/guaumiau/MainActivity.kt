package com.example.guaumiau

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guaumiau.data.local.AppDatabase
import com.example.guaumiau.data.repository.UserRepository
import com.example.guaumiau.ui.main.TaskListActivity
import com.example.guaumiau.ui.theme.GuauMiauTheme
import com.example.guaumiau.viewmodels.LoginViewModel
import com.example.guaumiau.viewmodels.RegisterViewModel
import com.example.guaumiau.views.LoginScreen
import com.example.guaumiau.views.RegisterScreen

/**
 * MainActivity - Actividad principal de la aplicación
 * Pantallas de Login y Registro con Jetpack Compose
 */
class MainActivity : ComponentActivity() {
    
    private lateinit var userRepository: UserRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar el repositorio
        val database = AppDatabase.getDatabase(applicationContext)
        userRepository = UserRepository(database.userDao())
        
        setContent {
            GuauMiauTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GuauMiauApp()
                }
            }
        }
    }
    
    @Composable
    fun GuauMiauApp() {
        var currentScreen by remember { mutableStateOf("login") }

        when (currentScreen) {
            "login" -> {
                val loginViewModel = remember { LoginViewModel(userRepository) }
                
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginSuccess = { userId ->
                        // Navegar a TaskListActivity con el userId
                        val intent = Intent(this@MainActivity, TaskListActivity::class.java).apply {
                            putExtra(TaskListActivity.EXTRA_USER_ID, userId)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        finish()
                    },
                    onNavigateToRegister = {
                        currentScreen = "register"
                    }
                )
            }
            "register" -> {
                val registerViewModel = remember { RegisterViewModel(userRepository) }
                
                RegisterScreen(
                    viewModel = registerViewModel,
                    onRegisterSuccess = {
                        // Registro exitoso, volver a login
                        currentScreen = "login"
                    },
                    onNavigateToLogin = {
                        currentScreen = "login"
                    }
                )
            }
        }
    }
}

