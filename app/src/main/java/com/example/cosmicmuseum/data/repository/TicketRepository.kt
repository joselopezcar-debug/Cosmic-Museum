package com.example.cosmicmuseum.data.repository

import com.example.cosmicmuseum.data.local.TicketDao
import com.example.cosmicmuseum.data.local.TicketEntity
import kotlinx.coroutines.flow.Flow

class TicketRepository(
    private val ticketDao: TicketDao,
    private val firestoreRepository: FirestoreTicketRepository,
    private val authRepository: AuthRepository
) {

    fun getAllTickets(): Flow<List<TicketEntity>> {
        return ticketDao.getAllTickets()
    }

    suspend fun getTicketById(
        id: Int
    ): TicketEntity? {

        return ticketDao.getTicketById(id)
    }

    suspend fun insertTicket(
        ticket: TicketEntity
    ) {

        val uid =
            authRepository.getCurrentUserId()
                ?: return

        val firestoreId =
            firestoreRepository.saveTicket(
                uid,
                ticket
            )

        ticketDao.insert(
            ticket.copy(
                firestoreId = firestoreId
            )
        )
    }

    suspend fun updateTicket(
        ticket: TicketEntity
    ) {

        val uid =
            authRepository.getCurrentUserId()
                ?: return

        firestoreRepository.saveTicket(
            uid,
            ticket
        )

        ticketDao.update(ticket)
    }

    suspend fun deleteTicket(
        ticket: TicketEntity
    ) {

        val uid =
            authRepository.getCurrentUserId()
                ?: return

        firestoreRepository.deleteTicket(
            uid,
            ticket.firestoreId
        )

        ticketDao.delete(ticket)
    }

    suspend fun syncFromFirestore() {

        val uid =
            authRepository.getCurrentUserId()
                ?: return

        val remoteTickets =
            firestoreRepository.getTickets(uid)

        ticketDao.deleteAll()

        ticketDao.insertAll(remoteTickets)
    }
}