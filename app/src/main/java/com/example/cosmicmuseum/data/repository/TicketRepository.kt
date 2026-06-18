package com.example.cosmicmuseum.data.repository

import com.example.cosmicmuseum.data.local.TicketDao
import com.example.cosmicmuseum.data.local.TicketEntity
import kotlinx.coroutines.flow.Flow

class TicketRepository(
    private val ticketDao: TicketDao
) {

    fun getAllTickets(): Flow<List<TicketEntity>> {
        return ticketDao.getAllTickets()
    }

    suspend fun getTicketById(id: Int): TicketEntity? {
        return ticketDao.getTicketById(id)
    }

    suspend fun insertTicket(ticket: TicketEntity) {
        ticketDao.insert(ticket)
    }

    suspend fun updateTicket(ticket: TicketEntity) {
        ticketDao.update(ticket)
    }

    suspend fun deleteTicket(ticket: TicketEntity) {
        ticketDao.delete(ticket)
    }
}