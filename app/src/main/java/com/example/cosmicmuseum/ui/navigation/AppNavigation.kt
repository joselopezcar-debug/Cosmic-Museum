package com.example.cosmicmuseum.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cosmicmuseum.ui.screens.EventsScreen
import com.example.cosmicmuseum.ui.screens.LoginScreen
import com.example.cosmicmuseum.ui.screens.RegisterScreen
import com.example.cosmicmuseum.ui.screens.TicketDetailScreen
import com.example.cosmicmuseum.ui.screens.TicketFormScreen
import com.example.cosmicmuseum.ui.screens.TicketListScreen
import com.example.cosmicmuseum.viewmodel.AuthViewModel
import com.example.cosmicmuseum.viewmodel.EventsViewModel
import com.example.cosmicmuseum.viewmodel.TicketDetailViewModel
import com.example.cosmicmuseum.viewmodel.TicketFormViewModel
import com.example.cosmicmuseum.viewmodel.TicketListViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    ticketListViewModel: TicketListViewModel,
    ticketDetailViewModel: TicketDetailViewModel,
    ticketFormViewModel: TicketFormViewModel,
    eventsViewModel: EventsViewModel
) {

    val navController = rememberNavController()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination =
            if (isLoggedIn)
                "tickets"
            else
                "login"
    ) {

        //Login
        composable("login") {

            LoginScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        //Registrar
        composable("register") {

            RegisterScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        // Lista de tickets
        composable("tickets") {

            TicketListScreen(
                navController = navController,
                viewModel = ticketListViewModel
            )
        }

        // Detalle
        composable(
            route = "detail/{ticketId}",
            arguments = listOf(
                navArgument("ticketId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val ticketId =
                backStackEntry.arguments?.getInt("ticketId") ?: 0

            TicketDetailScreen(
                navController = navController,
                ticketId = ticketId,
                viewModel = ticketDetailViewModel
            )
        }

        // Crear nuevo ticket
        composable("form") {

            TicketFormScreen(
                navController = navController,
                ticketId = null,
                viewModel = ticketFormViewModel
            )
        }

        // Editar ticket existente
        composable(
            route = "form/{ticketId}",
            arguments = listOf(
                navArgument("ticketId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val ticketId =
                backStackEntry.arguments?.getInt("ticketId")

            TicketFormScreen(
                navController = navController,
                ticketId = ticketId,
                viewModel = ticketFormViewModel
            )
        }

        // Pantalla NASA
        composable("events") {

            EventsScreen(
                navController = navController,
                viewModel = eventsViewModel
            )
        }
    }
}