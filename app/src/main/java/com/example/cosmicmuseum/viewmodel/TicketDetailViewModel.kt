package com.example.cosmicmuseum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicmuseum.data.local.TicketEntity
import com.example.cosmicmuseum.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TicketDetailViewModel(
    private val repository: TicketRepository
) : ViewModel() {

    private val _ticket = MutableStateFlow<TicketEntity?>(null)
    val ticket: StateFlow<TicketEntity?> = _ticket.asStateFlow()

    fun loadTicket(ticketId: Int) {
        viewModelScope.launch {
            _ticket.value = repository.getTicketById(ticketId)
        }
    }

    fun deleteTicket() {
        viewModelScope.launch {
            _ticket.value?.let {
                repository.deleteTicket(it)
            }
        }
    }
}