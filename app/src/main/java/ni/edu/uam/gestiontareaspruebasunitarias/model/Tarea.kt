package ni.edu.uam.gestiontareaspruebasunitarias.model

import java.util.UUID

enum class EstadoTarea {
    PENDIENTE,
    COMPLETADA
}

data class Tarea(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String,
    val descripcion: String,
    val estado: EstadoTarea = EstadoTarea.PENDIENTE
)
