package com.example.cosmicmuseum.data.repository

import com.example.cosmicmuseum.data.local.TicketEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreTicketRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun saveTicket(
        uid: String,
        ticket: TicketEntity
    ): String {

        return if (ticket.firestoreId.isBlank()) {

            val docRef = db.collection("users")
                .document(uid)
                .collection("tickets")
                .document()

            val firestoreId = docRef.id

            docRef.set(
                ticket.copy(
                    firestoreId = firestoreId
                )
            ).await()

            firestoreId

        } else {

            db.collection("users")
                .document(uid)
                .collection("tickets")
                .document(ticket.firestoreId)
                .set(ticket)
                .await()

            ticket.firestoreId
        }
    }

    suspend fun deleteTicket(
        uid: String,
        firestoreId: String
    ) {

        if (firestoreId.isNotBlank()) {

            db.collection("users")
                .document(uid)
                .collection("tickets")
                .document(firestoreId)
                .delete()
                .await()
        }
    }

    suspend fun getTickets(
        uid: String
    ): List<TicketEntity> {

        val snapshot =
            db.collection("users")
                .document(uid)
                .collection("tickets")
                .get()
                .await()

        return snapshot.documents.mapNotNull {

            it.toObject(
                TicketEntity::class.java
            )
        }
    }
}