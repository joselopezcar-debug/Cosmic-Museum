package com.example.cosmicmuseum.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cosmicmuseum.viewmodel.TicketDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    navController: NavController,
    ticketId: Int,
    viewModel: TicketDetailViewModel
) {

    val ticket by viewModel.ticket.collectAsState()

    LaunchedEffect(ticketId) {
        viewModel.loadTicket(ticketId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Detalle de Entrada")
                }
            )
        }
    ) { paddingValues ->

        ticket?.let { currentTicket ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Visitante: ${currentTicket.nombreVisitante}"
                )

                Text(
                    text = "Fecha: ${currentTicket.fechaVisita}"
                )

                Text(
                    text = "Tipo: ${currentTicket.tipoEntrada}"
                )

                Text(
                    text = "Cantidad: ${currentTicket.cantidadPersonas}"
                )

                Text(
                    text = "Precio: ${currentTicket.precio}"
                )

                Text(
                    text = "Código: ${currentTicket.codigoReserva}"
                )

                Text(
                    text = "Estado: ${currentTicket.estado}"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate(
                            "form/${currentTicket.id}"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Editar")
                }

                Button(
                    onClick = {
                        viewModel.deleteTicket()
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar")
                }
            }

        } ?: run {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                Text(
                    text = "Ticket no encontrado",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}