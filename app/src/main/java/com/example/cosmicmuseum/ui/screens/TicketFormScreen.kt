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
import java.text.SimpleDateFormat
import java.util.Locale

private fun calcularPrecio(
    tipoEntrada: String,
    cantidad: Int
): Double {

    val precioUnitario = when (tipoEntrada) {
        "General" -> 25.0
        "Estudiante" -> 15.0
        "VIP" -> 50.0
        "Evento Especial" -> 80.0
        else -> 0.0
    }

    return precioUnitario * cantidad
}

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
    var estado by remember { mutableStateOf("") }

    val tiposEntrada = listOf(
        "General",
        "Estudiante",
        "VIP",
        "Evento Especial"
    )

    val estados = listOf(
        "Pendiente",
        "Confirmada",
        "Cancelada"
    )

    var expandedTipoEntrada by remember {
        mutableStateOf(false)
    }

    var expandedEstado by remember {
        mutableStateOf(false)
    }

    val formatter = remember {
        SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        )
    }

    LaunchedEffect(ticketId) {

        if (ticketId != null) {
            viewModel.loadTicket(ticketId)
        }
    }

    LaunchedEffect(currentTicket) {

        currentTicket?.let {

            nombreVisitante = it.nombreVisitante

            fechaVisita =
                formatter.format(it.fechaVisita)

            tipoEntrada = it.tipoEntrada

            cantidadPersonas =
                it.cantidadPersonas.toString()

            estado = it.estado
        }
    }

    val cantidadCalculada =
        cantidadPersonas.toIntOrNull() ?: 0

    val precioCalculado =
        calcularPrecio(
            tipoEntrada,
            cantidadCalculada
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (ticketId == null)
                            "Nueva Reserva"
                        else
                            "Editar Reserva"
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
                .verticalScroll(
                    rememberScrollState()
                ),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = nombreVisitante,
                onValueChange = {
                    nombreVisitante = it
                },
                label = {
                    Text("Nombre del visitante")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fechaVisita,
                onValueChange = {
                    fechaVisita = it
                },
                label = {
                    Text("Fecha (dd/MM/yyyy)")
                },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expandedTipoEntrada,
                onExpandedChange = {
                    expandedTipoEntrada =
                        !expandedTipoEntrada
                }
            ) {

                OutlinedTextField(
                    value = tipoEntrada,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Tipo de entrada")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults
                            .TrailingIcon(
                                expanded =
                                    expandedTipoEntrada
                            )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedTipoEntrada,
                    onDismissRequest = {
                        expandedTipoEntrada = false
                    }
                ) {

                    tiposEntrada.forEach { opcion ->

                        DropdownMenuItem(
                            text = {
                                Text(opcion)
                            },
                            onClick = {

                                tipoEntrada = opcion

                                expandedTipoEntrada = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = cantidadPersonas,
                onValueChange = {
                    cantidadPersonas = it
                },
                label = {
                    Text("Cantidad de personas")
                },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expandedEstado,
                onExpandedChange = {
                    expandedEstado =
                        !expandedEstado
                }
            ) {

                OutlinedTextField(
                    value = estado,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Estado")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults
                            .TrailingIcon(
                                expanded =
                                    expandedEstado
                            )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedEstado,
                    onDismissRequest = {
                        expandedEstado = false
                    }
                ) {

                    estados.forEach { opcion ->

                        DropdownMenuItem(
                            text = {
                                Text(opcion)
                            },
                            onClick = {

                                estado = opcion

                                expandedEstado = false
                            }
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Resumen de la Reserva",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Precio Total: S/ $precioCalculado"
                    )
                }
            }

            Button(
                onClick = {

                    val fechaMillis =
                        try {
                            formatter.parse(fechaVisita)?.time
                                ?: System.currentTimeMillis()
                        } catch (e: Exception) {
                            System.currentTimeMillis()
                        }

                    val codigoReserva =
                        currentTicket?.codigoReserva
                            ?: "CM-${System.currentTimeMillis()}"

                    val ticket = TicketEntity(
                        id = ticketId ?: 0,
                        nombreVisitante =
                            nombreVisitante,
                        fechaVisita =
                            fechaMillis,
                        tipoEntrada =
                            tipoEntrada,
                        cantidadPersonas =
                            cantidadCalculada,
                        codigoReserva =
                            codigoReserva,
                        precioTotal =
                            precioCalculado,
                        estado =
                            estado
                    )

                    viewModel.saveTicket(ticket)

                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Guardar Reserva")
            }
        }
    }
}