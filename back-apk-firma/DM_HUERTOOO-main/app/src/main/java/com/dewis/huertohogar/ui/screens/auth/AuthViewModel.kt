package com.dewis.huertohogar.ui.screens.auth

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dewis.dm_huertohogar_ex3.data.datastore.SessionDataStore
import com.dewis.huertohogar.data.remote.ApiClient
import com.dewis.huertohogar.data.remote.LoginBody
import com.dewis.huertohogar.data.remote.RegisterBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val nameError: String? = null,
    val isLoading: Boolean = false,
    val generalError: String? = null
)

class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val session = SessionDataStore(app)
    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    fun onEmail(v: String) {
        _ui.value = _ui.value.copy(
            email = v.trim(),
            emailError = null,
            generalError = null
        )
    }

    fun onPass(v: String) {
        _ui.value = _ui.value.copy(
            password = v.trim(),
            passwordError = null,
            generalError = null
        )
    }

    fun onName(v: String) {
        _ui.value = _ui.value.copy(
            name = v.trim(),
            nameError = null,
            generalError = null
        )
    }

    private fun validateLogin(): Boolean {
        val s = _ui.value
        val emailOk = Patterns.EMAIL_ADDRESS.matcher(s.email).matches()
        val passOk = s.password.length >= 6

        _ui.value = s.copy(
            emailError = if (!emailOk) "Email inválido" else null,
            passwordError = if (!passOk) "Mínimo 6 caracteres" else null
        )

        return emailOk && passOk
    }

    private fun validateRegister(): Boolean {
        val s = _ui.value
        val emailOk = Patterns.EMAIL_ADDRESS.matcher(s.email).matches()
        val passOk = s.password.length >= 6
        val nameOk = s.name.isNotBlank()

        _ui.value = s.copy(
            emailError = if (!emailOk) "Email inválido" else null,
            passwordError = if (!passOk) "Mínimo 6 caracteres" else null,
            nameError = if (!nameOk) "El nombre es obligatorio" else null
        )

        return emailOk && passOk && nameOk
    }

    fun login(onSuccess: () -> Unit) = viewModelScope.launch {
        if (!validateLogin()) return@launch
        try {
            _ui.value = _ui.value.copy(isLoading = true, generalError = null)

            val body = LoginBody(
                email = _ui.value.email,
                password = _ui.value.password
            )

            val resp = ApiClient.authApi.login(body)

            session.setEmail(resp.email)
            session.setToken(resp.token)

            _ui.value = _ui.value.copy(isLoading = false)
            onSuccess()

        } catch (e: Exception) {
            _ui.value = _ui.value.copy(
                isLoading = false,
                generalError = "Error de autenticación. Revisa tus credenciales o la conexión."
            )
        }
    }

    fun register(onSuccess: () -> Unit) = viewModelScope.launch {
        if (!validateRegister()) return@launch
        try {
            _ui.value = _ui.value.copy(isLoading = true, generalError = null)

            val body = RegisterBody(
                email = _ui.value.email,
                password = _ui.value.password,
                nombre = _ui.value.name
            )

            val resp = ApiClient.authApi.register(body)

            session.setEmail(resp.email)
            session.setToken(resp.token)

            _ui.value = _ui.value.copy(isLoading = false)
            onSuccess()

        } catch (e: Exception) {
            _ui.value = _ui.value.copy(
                isLoading = false,
                generalError = "Error al registrar. Revisa los datos o la conexión."
            )
        }
    }

    fun guest(onSuccess: () -> Unit) = viewModelScope.launch {
        // Invitado sin token => no puede usar /productos ni /carrito del backend
        session.setEmail("invitado@huertohogar.cl")
        session.setToken(null)
        onSuccess()
    }
}
