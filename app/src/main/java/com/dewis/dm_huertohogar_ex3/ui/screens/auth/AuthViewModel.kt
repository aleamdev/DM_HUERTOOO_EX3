package com.dewis.dm_huertohogar_ex3.ui.screens.auth

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dewis.dm_huertohogar_ex3.data.datastore.SessionDataStore
import com.dewis.dm_huertohogar_ex3.data.remote.ApiClient
import com.dewis.dm_huertohogar_ex3.data.remote.LoginBody
import com.dewis.dm_huertohogar_ex3.data.remote.RegisterBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

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

    // --------------------
    //  INPUT HANDLERS
    // --------------------

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

    // --------------------
    //  VALIDACIONES
    // --------------------

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

    // --------------------
    //  LOGIN
    // --------------------

    fun login(onSuccess: () -> Unit) = viewModelScope.launch {
        if (!validateLogin()) return@launch

        try {
            _ui.value = _ui.value.copy(isLoading = true, generalError = null)

            val body = LoginBody(
                email = _ui.value.email,
                password = _ui.value.password
            )

            val resp = ApiClient.authApi.login(body)

            // Guardamos sesión
            session.setEmail(resp.email)
            session.setToken(resp.token)

            _ui.value = _ui.value.copy(isLoading = false)
            onSuccess()

        } catch (e: HttpException) {
            val code = e.code()
            val msg = when (code) {
                400 -> "Datos inválidos. Revisa email y contraseña."
                401 -> "Credenciales incorrectas."
                else -> "Error de servidor ($code): ${e.message()}"
            }
            _ui.value = _ui.value.copy(
                isLoading = false,
                generalError = msg
            )
        } catch (e: IOException) {
            _ui.value = _ui.value.copy(
                isLoading = false,
                generalError = "Error de red. Verifica tu conexión."
            )
        } catch (e: Exception) {
            _ui.value = _ui.value.copy(
                isLoading = false,
                generalError = "Error inesperado en login: ${e.message}"
            )
        }
    }

    // --------------------
    //  REGISTER
    // --------------------

    fun register(onSuccess: () -> Unit) = viewModelScope.launch {
        if (!validateRegister()) return@launch

        try {
            _ui.value = _ui.value.copy(isLoading = true, generalError = null)

            val body = RegisterBody(
                email = _ui.value.email,
                password = _ui.value.password,
                nombre = _ui.value.name           // 👈 IMPORTANTE: nombre
            )

            val resp = ApiClient.authApi.register(body)

            // Guardar sesión con el usuario recién creado
            session.setEmail(resp.email)
            session.setToken(resp.token)

            _ui.value = _ui.value.copy(isLoading = false)
            onSuccess()

        } catch (e: HttpException) {
            val code = e.code()
            // Opcional: intentar leer mensaje del backend
            val serverMsg = try {
                e.response()?.errorBody()?.string()
            } catch (_: Exception) {
                null
            }

            val msg = when (code) {
                400 -> {
                    // tu backend suele mandar RuntimeException con mensaje específico
                    if (serverMsg != null && serverMsg.contains("El email ya está registrado"))
                        "El email ya está registrado."
                    else
                        "Datos inválidos o email ya registrado."
                }
                else -> "Error de servidor ($code): ${e.message()}"
            }

            _ui.value = _ui.value.copy(
                isLoading = false,
                generalError = msg
            )

        } catch (e: IOException) {
            _ui.value = _ui.value.copy(
                isLoading = false,
                generalError = "Error de red. Verifica tu conexión."
            )
        } catch (e: Exception) {
            _ui.value = _ui.value.copy(
                isLoading = false,
                generalError = "Error inesperado al registrar: ${e.message}"
            )
        }
    }

    // --------------------
    //  INVITADO
    // --------------------

    fun guest(onSuccess: () -> Unit) = viewModelScope.launch {
        // Invitado sin token => no puede usar /productos ni /carrito protegidos
        session.setEmail("invitado@huertohogar.cl")
        session.setToken(null)
        onSuccess()
    }
}
