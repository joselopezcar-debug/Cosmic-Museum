package com.example.cosmicmuseum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicmuseum.data.local.TicketEntity
import com.example.cosmicmuseum.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TicketFormViewModel(
    private val repository: TicketRepository
) : ViewModel() {

    private val _currentTicket = MutableStateFlow<TicketEntity?>(null)
    val currentTicket: StateFlow<TicketEntity?> = _currentTicket.asStateFlow()

    fun loadTicket(ticketId: Int) {
        viewModelScope.launch {
            _currentTicket.value = repository.getTicketById(ticketId)
        }
    }

    fun saveTicket(ticket: TicketEntity) {
        viewModelScope.launch {

            if (ticket.id == 0) {
                repository.insertTicket(ticket)
            } else {
                repository.updateTicket(ticket)
            }
        }
    }
}