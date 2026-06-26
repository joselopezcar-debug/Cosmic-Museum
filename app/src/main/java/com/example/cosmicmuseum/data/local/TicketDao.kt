package com.example.cosmicmuseum.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {

    @Insert
    suspend fun insert(ticket: TicketEntity): Long

    @Update
    suspend fun update(ticket: TicketEntity): Int

    @Delete
    suspend fun delete(ticket: TicketEntity): Int

    @Query("SELECT * FROM tickets")
    fun getAllTickets(): Flow<List<TicketEntity>>

    @Query("SELECT * FROM tickets WHERE id = :id")
    suspend fun getTicketById(id: Int): TicketEntity?

    @Query(
        "SELECT * FROM tickets WHERE firestoreId = :firestoreId"
    )
    suspend fun getTicketByFirestoreId(
        firestoreId: String
    ): TicketEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        tickets: List<TicketEntity>
    )

    @Query("DELETE FROM tickets")
    suspend fun deleteAll()
}