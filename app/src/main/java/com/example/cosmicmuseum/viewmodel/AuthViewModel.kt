package com.example.cosmicmuseum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicmuseum.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _isLoggedIn =
        MutableStateFlow(repository.isUserLoggedIn())

    val isLoggedIn: StateFlow<Boolean> =
        _isLoggedIn.asStateFlow()

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()

    fun register(
        email: String,
        password: String
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                repository.register(
                    email,
                    password
                )

                _isLoggedIn.value = true

            } catch (e: Exception) {

                _error.value =
                    e.message ?: "Error al registrar usuario"

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun login(
        email: String,
        password: String
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                repository.login(
                    email,
                    password
                )

                _isLoggedIn.value = true

            } catch (e: Exception) {

                _error.value =
                    e.message ?: "Error al iniciar sesión"

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun logout() {

        repository.logout()

        _isLoggedIn.value = false
    }
}