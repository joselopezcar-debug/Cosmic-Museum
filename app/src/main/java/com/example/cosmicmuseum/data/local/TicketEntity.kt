package com.example.cosmicmuseum.data.local

import androidx.room.*

@Entity(tableName = "tickets")
data class TicketEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val firestoreId: String = "",

    val nombreVisitante: String = "",

    val fechaVisita: Long = 0L,

    val tipoEntrada: String = "",

    val cantidadPersonas: Int = 0,

    val codigoReserva: String = "",

    val precioTotal: Double = 0.0,

    val estado: String = ""
)