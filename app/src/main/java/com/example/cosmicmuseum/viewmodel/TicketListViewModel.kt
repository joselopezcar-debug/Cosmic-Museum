package com.example.cosmicmuseum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicmuseum.data.local.TicketEntity
import com.example.cosmicmuseum.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TicketListViewModel(
    private val repository: TicketRepository
) : ViewModel() {

    private val _tickets = MutableStateFlow<List<TicketEntity>>(emptyList())
    val tickets: StateFlow<List<TicketEntity>> = _tickets.asStateFlow()

    init {
        loadTickets()
    }

    private fun loadTickets() {
        viewModelScope.launch {
            repository.getAllTickets().collect { ticketList ->
                _tickets.value = ticketList
            }
        }
    }

    fun deleteTicket(ticket: TicketEntity) {
        viewModelScope.launch {
            repository.deleteTicket(ticket)
        }
    }
}