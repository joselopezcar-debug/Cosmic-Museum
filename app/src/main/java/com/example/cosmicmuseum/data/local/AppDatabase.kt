package com.example.cosmicmuseum.data.local

import androidx.room.*

@Database(
    entities = [TicketEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ticketDao(): TicketDao
}