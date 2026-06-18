package com.example.cosmicmuseum.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cosmicmuseum.data.local.TicketEntity
import com.example.cosmicmuseum.viewmodel.TicketFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketFormScreen(
    navController: NavController,
    ticketId: Int?,
    viewModel: TicketFormViewModel
) {

    val currentTicket by viewModel.currentTicket.collectAsState()

    var nombreVisitante by remember { mutableStateOf("") }
    var fechaVisita by remember { mutableStateOf("") }
    var tipoEntrada by remember { mutableStateOf("") }
    var cantidadPersonas by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var codigoReserva by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }

    LaunchedEffect(ticketId) {

        if (ticketId != null) {

            viewModel.loadTicket(ticketId)
        }
    }

    LaunchedEffect(currentTicket) {

        currentTicket?.let {

            nombreVisitante = it.nombreVisitante
            fechaVisita = it.fechaVisita
            tipoEntrada = it.tipoEntrada
            cantidadPersonas = it.cantidadPersonas.toString()
            precio = it.precio.toString()
            codigoReserva = it.codigoReserva
            estado = it.estado
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (ticketId == null)
                            "Nueva Entrada"
                        else
                            "Editar Entrada"
                    )
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = nombreVisitante,
                onValueChange = { nombreVisitante = it },
                label = { Text("Nombre Visitante") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fechaVisita,
                onValueChange = { fechaVisita = it },
                label = { Text("Fecha Visita") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = tipoEntrada,
                onValueChange = { tipoEntrada = it },
                label = { Text("Tipo Entrada") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cantidadPersonas,
                onValueChange = { cantidadPersonas = it },
                label = { Text("Cantidad Personas") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = codigoReserva,
                onValueChange = { codigoReserva = it },
                label = { Text("Código Reserva") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado,
                onValueChange = { estado = it },
                label = { Text("Estado") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {

                    val ticket = TicketEntity(
                        id = ticketId ?: 0,
                        nombreVisitante = nombreVisitante,
                        fechaVisita = fechaVisita,
                        tipoEntrada = tipoEntrada,
                        cantidadPersonas = cantidadPersonas.toIntOrNull() ?: 0,
                        precio = precio.toDoubleOrNull() ?: 0.0,
                        codigoReserva = codigoReserva,
                        estado = estado
                    )

                    viewModel.saveTicket(ticket)

                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Guardar")
            }
        }
    }
}