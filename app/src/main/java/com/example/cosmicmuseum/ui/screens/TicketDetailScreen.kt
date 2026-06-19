package com.example.cosmicmuseum.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cosmicmuseum.viewmodel.TicketDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

            val fechaFormateada = remember(currentTicket.fechaVisita) {
                SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                ).format(
                    Date(currentTicket.fechaVisita)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Código de Reserva: ${currentTicket.codigoReserva}"
                )

                Text(
                    text = "Visitante: ${currentTicket.nombreVisitante}"
                )

                Text(
                    text = "Fecha: $fechaFormateada"
                )

                Text(
                    text = "Tipo de Entrada: ${currentTicket.tipoEntrada}"
                )

                Text(
                    text = "Cantidad de Personas: ${currentTicket.cantidadPersonas}"
                )

                Text(
                    text = "Precio Total: S/ ${currentTicket.precioTotal}"
                )

                Text(
                    text = "Estado: ${currentTicket.estado}"
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

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

                OutlinedButton(
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