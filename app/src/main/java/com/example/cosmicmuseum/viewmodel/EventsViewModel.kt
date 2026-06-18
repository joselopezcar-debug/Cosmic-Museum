package com.example.cosmicmuseum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicmuseum.data.remote.NasaDto
import com.example.cosmicmuseum.data.repository.NasaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventsViewModel(
    private val repository: NasaRepository
) : ViewModel() {

    private val _nasaData = MutableStateFlow<NasaDto?>(null)
    val nasaData: StateFlow<NasaDto?> = _nasaData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadApod() {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                _nasaData.value = repository.getApod()

            } catch (e: Exception) {

                _error.value = e.message

            } finally {

                _isLoading.value = false

            }
        }
    }
}