package com.example.cosmicmuseum.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cosmicmuseum.ui.screens.TicketDetailScreen
import com.example.cosmicmuseum.ui.screens.EventsScreen
import com.example.cosmicmuseum.ui.screens.TicketFormScreen
import com.example.cosmicmuseum.ui.screens.TicketListScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "tickets"
    ) {

        composable("tickets") {

            TicketListScreen(
                navController = navController
            )
        }

        composable(
            route = "detail/{ticketId}",
            arguments = listOf(
                navArgument("ticketId") {
                    type = NavType.IntType
                }
            )
        ) {

            TicketDetailScreen(
                navController = navController
            )
        }

        composable("form") {

            TicketFormScreen(
                navController = navController
            )
        }

        composable(
            route = "form/{ticketId}",
            arguments = listOf(
                navArgument("ticketId") {
                    type = NavType.IntType
                }
            )
        ) {

            TicketFormScreen(
                navController = navController
            )
        }

        composable("events") {

            EventsScreen(
                navController = navController
            )
        }
    }
}