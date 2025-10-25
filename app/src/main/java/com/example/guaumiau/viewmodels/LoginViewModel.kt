package com.example.guaumiau.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guaumiau.data.Validator
import com.example.guaumiau.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Login
 */
class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null
        )
    }

    fun onLoginClick() {
        val state = _uiState.value

        // Validar email
        val emailValidation = Validator.validateLoginEmail(state.email)
        if (!emailValidation.isValid) {
            _uiState.value = state.copy(emailError = emailValidation.errorMessage)
            return
        }

        // Validar password
        val passwordValidation = Validator.validateLoginPassword(state.password)
        if (!passwordValidation.isValid) {
            _uiState.value = state.copy(passwordError = passwordValidation.errorMessage)
            return
        }

        // Autenticar con la base de datos
        _uiState.value = state.copy(isLoading = true, generalError = null)
        
        viewModelScope.launch {
            try {
                val user = userRepository.authenticateUser(state.email, state.password)
                
                if (user != null) {
                    // Login exitoso
                    _uiState.value = state.copy(
                        isLoading = false,
                        loginSuccess = true,
                        userId = user.email // Guardamos el email como userId
                    )
                } else {
                    // Credenciales incorrectas
                    _uiState.value = state.copy(
                        isLoading = false,
                        generalError = "El nombre de usuario (correo) o la contraseña son incorrectos"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isLoading = false,
                    generalError = "Error al iniciar sesión: ${e.message}"
                )
            }
        }
    }

    fun onForgotPasswordClick() {
        // Aquí iría la navegación a recuperación de contraseña
        _uiState.value = _uiState.value.copy(
            generalError = "Funcionalidad de recuperación de contraseña próximamente"
        )
    }

    fun clearErrors() {
        _uiState.value = _uiState.value.copy(
            emailError = null,
            passwordError = null,
            generalError = null
        )
    }
}

/**
 * Estado de UI para Login
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val userId: String? = null
)
