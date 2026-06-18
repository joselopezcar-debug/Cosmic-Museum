package com.example.cosmicmuseum.data.local

import androidx.room.*

@Entity(tableName = "tickets")
data class TicketEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombreVisitante: String,

    val fechaVisita: String,

    val tipoEntrada: String,

    val cantidadPersonas: Int,

    val precio: Double,

    val codigoReserva: String,

    val estado: String
)