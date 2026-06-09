package ni.edu.uam.gestiontareaspruebasunitarias.repository

import ni.edu.uam.gestiontareaspruebasunitarias.model.EstadoTarea
import ni.edu.uam.gestiontareaspruebasunitarias.model.Tarea

class GestorTareas {
    private val _tareas = mutableListOf<Tarea>()
    val tareas: List<Tarea> get() = _tareas

    fun agregarTarea(titulo: String, descripcion: String) {
        val nuevaTarea = Tarea(titulo = titulo, descripcion = descripcion)
        _tareas.add(nuevaTarea)
    }

    fun eliminarTarea(id: String) {
        _tareas.removeIf { it.id == id }
    }

    fun marcarComoCompletada(id: String) {
        val index = _tareas.indexOfFirst { it.id == id }
        if (index != -1) {
            val tarea = _tareas[index]
            _tareas[index] = tarea.copy(estado = EstadoTarea.COMPLETADA)
        }
    }

    fun obtenerTareasPendientes(): List<Tarea> {
        return _tareas.filter { it.estado == EstadoTarea.PENDIENTE }
    }

    fun contarTareasPendientes(): Int {
        return _tareas.count { it.estado == EstadoTarea.PENDIENTE }
    }
}
