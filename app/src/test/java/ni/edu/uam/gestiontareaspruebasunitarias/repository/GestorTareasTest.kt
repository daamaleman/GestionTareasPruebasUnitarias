package ni.edu.uam.gestiontareaspruebasunitarias.repository

import ni.edu.uam.gestiontareaspruebasunitarias.model.EstadoTarea
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GestorTareasTest {

    private lateinit var gestor: GestorTareas

    @Before
    fun setup() {
        gestor = GestorTareas()
    }

    @Test
    fun `agregar una tarea debe incrementar la lista en uno`() {
        gestor.agregarTarea("Tarea 1", "Descripción 1")
        assertEquals(1, gestor.tareas.size)
    }

    @Test
    fun `eliminar una tarea debe quitarla de la lista`() {
        gestor.agregarTarea("Tarea a eliminar", "Descripción")
        val id = gestor.tareas[0].id
        gestor.eliminarTarea(id)
        assertTrue(gestor.tareas.isEmpty())
    }

    @Test
    fun `marcar como completada debe cambiar el estado de la tarea`() {
        gestor.agregarTarea("Tarea pendiente", "Descripción")
        val id = gestor.tareas[0].id
        gestor.marcarComoCompletada(id)
        assertEquals(EstadoTarea.COMPLETADA, gestor.tareas[0].estado)
    }

    @Test
    fun `contar tareas pendientes debe retornar el valor correcto`() {
        gestor.agregarTarea("T1", "D1")
        gestor.agregarTarea("T2", "D2")
        gestor.marcarComoCompletada(gestor.tareas[0].id)
        assertEquals(1, gestor.contarTareasPendientes())
    }

    @Test
    fun `al iniciar el gestor debe tener cero tareas pendientes`() {
        assertEquals(0, gestor.contarTareasPendientes())
        assertTrue(gestor.tareas.isEmpty())
    }
}
