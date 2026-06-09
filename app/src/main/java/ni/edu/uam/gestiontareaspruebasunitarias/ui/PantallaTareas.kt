package ni.edu.uam.gestiontareaspruebasunitarias.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.gestiontareaspruebasunitarias.model.EstadoTarea
import ni.edu.uam.gestiontareaspruebasunitarias.model.Tarea
import ni.edu.uam.gestiontareaspruebasunitarias.ui.theme.DeepDarkBackground
import ni.edu.uam.gestiontareaspruebasunitarias.ui.theme.DeepDarkSurface
import ni.edu.uam.gestiontareaspruebasunitarias.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel = viewModel()) {
    val tareas by viewModel.tareas.collectAsState()
    val pendientesCount by viewModel.pendientesCount.collectAsState()
    var nuevoTitulo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepDarkBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "Gestión de Tareas",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = nuevoTitulo,
            onValueChange = { nuevoTitulo = it },
            label = { Text("Nueva tarea", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("textFieldTarea"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.DarkGray,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (nuevoTitulo.isNotBlank()) {
                    viewModel.agregarTarea(nuevoTitulo, "")
                    nuevoTitulo = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("botonAgregarTarea")
        ) {
            Text("Agregar Tarea")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tareas pendientes: $pendientesCount",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 14.sp,
            modifier = Modifier.testTag("contadorPendientes")
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("listaTareas"),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tareas) { tarea ->
                ItemTarea(
                    tarea = tarea,
                    onMarcarCompletada = { viewModel.marcarComoCompletada(tarea.id) },
                    onEliminar = { viewModel.eliminarTarea(tarea.id) }
                )
            }
        }
    }
}

@Composable
fun ItemTarea(
    tarea: Tarea,
    onMarcarCompletada: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DeepDarkSurface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = tarea.estado == EstadoTarea.COMPLETADA,
                onCheckedChange = { if (it) onMarcarCompletada() },
                modifier = Modifier.testTag("checkbox_${tarea.id}")
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tarea.titulo,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (tarea.estado == EstadoTarea.COMPLETADA) 
                        androidx.compose.ui.text.style.TextDecoration.LineThrough 
                    else null
                )
            }

            IconButton(
                onClick = onEliminar,
                modifier = Modifier.testTag("botonEliminar_${tarea.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Red.copy(alpha = 0.7f)
                )
            }
        }
    }
}
