package com.example.cosmicmuseum.data.local

import androidx.room.*

@Entity(tableName = "tickets")
data class TicketEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombreVisitante: String,

    val fechaVisita: Long,

    val tipoEntrada: String,

    val cantidadPersonas: Int,

    val codigoReserva: String,

    val precioTotal: Double,

    val estado: String
)