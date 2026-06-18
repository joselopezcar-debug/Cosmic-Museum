package com.example.cosmicmuseum.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cosmicmuseum.data.local.TicketEntity
import com.example.cosmicmuseum.viewmodel.TicketListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    navController: NavController,
    viewModel: TicketListViewModel
) {

    val tickets by viewModel.tickets.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Cosmic Museum")
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate("events")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = "Eventos"
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("form")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Ticket"
                )
            }
        }

    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
        ) {

            items(tickets) { ticket ->

                TicketItem(
                    ticket = ticket,
                    onClick = {
                        navController.navigate(
                            "detail/${ticket.id}"
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun TicketItem(
    ticket: TicketEntity,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onClick()
            }
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = ticket.nombreVisitante,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Fecha: ${ticket.fechaVisita}"
            )

            Text(
                text = "Tipo: ${ticket.tipoEntrada}"
            )

            Text(
                text = "Código: ${ticket.codigoReserva}"
            )
        }
    }
}