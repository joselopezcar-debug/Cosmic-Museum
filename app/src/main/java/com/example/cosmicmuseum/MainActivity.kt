package com.example.cosmicmuseum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.cosmicmuseum.data.local.AppDatabase
import com.example.cosmicmuseum.data.repository.AuthRepository
import com.example.cosmicmuseum.data.repository.FirestoreTicketRepository
import com.example.cosmicmuseum.data.repository.NasaRepository
import com.example.cosmicmuseum.data.repository.TicketRepository
import com.example.cosmicmuseum.ui.navigation.AppNavigation
import com.example.cosmicmuseum.ui.theme.CosmicMuseumTheme
import com.example.cosmicmuseum.viewmodel.AuthViewModel
import com.example.cosmicmuseum.viewmodel.EventsViewModel
import com.example.cosmicmuseum.viewmodel.TicketDetailViewModel
import com.example.cosmicmuseum.viewmodel.TicketFormViewModel
import com.example.cosmicmuseum.viewmodel.TicketListViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Room Database
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "cosmic_museum_db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val ticketDao = database.ticketDao()

        // Repositories
        val authRepository = AuthRepository()

        val firestoreTicketRepository =
            FirestoreTicketRepository()

        val ticketRepository =
            TicketRepository(
                ticketDao,
                firestoreTicketRepository,
                authRepository
            )
        val nasaRepository = NasaRepository()

        // ViewModels
        val ticketListViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TicketListViewModel(ticketRepository) as T
                }
            }
        )[TicketListViewModel::class.java]

        val ticketDetailViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TicketDetailViewModel(ticketRepository) as T
                }
            }
        )[TicketDetailViewModel::class.java]

        val ticketFormViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TicketFormViewModel(ticketRepository) as T
                }
            }
        )[TicketFormViewModel::class.java]

        val eventsViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EventsViewModel(nasaRepository) as T
                }
            }
        )[EventsViewModel::class.java]

        val authViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {

                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>
                ): T {

                    return AuthViewModel(
                        authRepository,
                        ticketRepository
                    ) as T
                }
            }
        )[AuthViewModel::class.java]

        setContent {

            CosmicMuseumTheme {

                AppNavigation(
                    authViewModel = authViewModel,
                    ticketListViewModel = ticketListViewModel,
                    ticketDetailViewModel = ticketDetailViewModel,
                    ticketFormViewModel = ticketFormViewModel,
                    eventsViewModel = eventsViewModel,
                )
            }
        }
    }
}