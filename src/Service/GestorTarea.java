package Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Enums.Estado;
import Enums.Prioridad;
import Model.Tarea;
import Repositories.CSVManager;

public class GestorTarea {

    private final CSVManager csvManager;
    private final Map<Long, Tarea> map;
    private long nextId = 1;

    public GestorTarea(CSVManager csvManager) {
        this.csvManager = csvManager;
        this.map = new HashMap<>();
    }

    // cargolastareas
    public void cargarTareas() {
        List<Tarea> lista = csvManager.leerTareas();
        map.clear();
        long max = 0;
        for (Tarea t : lista) {
            map.put(t.getId(), t);
            if (t.getId() > max) {
                max = t.getId();
            }
        }
        nextId = max + 1;
        System.out.println(map.size() + " tareas cargadas.");
    }

    // listo

    public void listarTareas() {
        for (Tarea t : map.values()) {
            System.out.println(
                    "ID: " + t.getId() + 
                    " | Título: " + t.getTitulo() +
                    " | Descripción: " + t.getDescripcion() +
                    " | Estado: " + t.getEstado() +
                    " | Prioridad: " + t.getPrioridad() +
                    " | Inicio: " + t.getFecha_inicio() +
                    " | Vencimiento: " + t.getFecha_vencimiento());
        }
    }

    // agora creamos 💪
    public void crearTarea(String titulo, String descripcion, String fecha_inicio_str, String fecha_vencimiento_str,
            Estado estado, Prioridad prioridad) {
        for (Tarea t : map.values()) { // verificamos titulos repetidos
            if (t.getTitulo().equalsIgnoreCase(titulo.trim())) {
                System.out.println("No se pueden crear tareas con el mismo titulo");
                return;
            }
        }

        LocalDate fecha_inicio = null;
        LocalDate fecha_vencimiento = null;

        try {
            if (fecha_inicio_str != null && !fecha_inicio_str.trim().isEmpty()) {
                fecha_inicio = LocalDate.parse(fecha_inicio_str.trim());
            }

            if (fecha_vencimiento_str != null && !fecha_vencimiento_str.trim().isEmpty()) {
                fecha_vencimiento = LocalDate.parse(fecha_vencimiento_str.trim());
            }
        } catch (Exception e) {
            System.out.println("Error. El formato de la fecha debe ser YYYY-MM-DD" + e.getMessage());
            return;
        }

        // ahora si.
        try {
            Tarea nuevaTarea = new Tarea(nextId, titulo.trim(), descripcion.trim(), fecha_inicio, fecha_vencimiento,
                    estado, prioridad);

            map.put(nuevaTarea.getId(), nuevaTarea);
            nextId++;

            // guardamos

            List<Tarea> lista = new ArrayList<>(map.values());
            csvManager.guardarTareas(lista);
            System.out.println("Tarea creada ✅. ID: " + nuevaTarea.getId());

        } catch (Exception e) {
            System.out.println("Error al crear tarea" + e.getMessage());
        }
    }

    // Eliminar tarea
    public void eliminarTarea(long id) {
        if (!map.containsKey(id)) {
            System.out.println("No existe una tarea con ID: " + id);
            return;
        }

        map.remove(id);

        List<Tarea> lista = new ArrayList<>(map.values());
        csvManager.guardarTareas(lista);

        System.out.println("Tarea eliminada, ID: " + id);
    }

    // editar tarea

    public void editarTarea(long id, String newTitulo, String newDescripcion, String newFecha_inicio_str,
            String newFecha_vencimiento_str, Estado newEstado, Prioridad newPrioridad) {

        Tarea tarea = map.get(id);
        if (tarea == null) {
            System.out.println("No existe una tarea con ese id: " + id);
            return;
        }

        String tituloActual = tarea.getTitulo();
        String descActual = tarea.getDescripcion();
        LocalDate fecha_inicio_act = tarea.getFecha_inicio();
        LocalDate fecha_vencimiento_act = tarea.getFecha_vencimiento();
        Estado estActual = tarea.getEstado();
        Prioridad prioActual = tarea.getPrioridad();

        // aplicar cambios en cada uno

        // TITULO

        if (newTitulo != null) {
            String titulo = newTitulo.trim();
            if (titulo.isEmpty()) {
                System.out.println("El titulo no puede quedar vacio.");
                return;
            }
            tituloActual = titulo;
        }

        // DESCRIPCION

        if (newDescripcion != null) {
            descActual = newDescripcion.trim();
        }

        // fechas
        try {
            if (newFecha_inicio_str != null && !newFecha_inicio_str.trim().isEmpty()) {

                fecha_inicio_act = LocalDate.parse(newFecha_inicio_str.trim());
            }
            if (newFecha_vencimiento_str != null && !newFecha_vencimiento_str.trim().isEmpty()) {

                fecha_vencimiento_act = LocalDate.parse(newFecha_vencimiento_str.trim());
            }

        } catch (Exception e) {
            System.out.println("El formato de la fecha debe ser YYYY-MM-DD");
            return;
        }
        // fecha inicio y vencimiento.

        if (fecha_inicio_act != null && fecha_vencimiento_act != null
                && fecha_vencimiento_act.isBefore(fecha_inicio_act)) {
            System.out.println("La fecha de vencimiento no puede ser anterior a la fecha de inicio");
            return;
        }

        // enums
        if (newEstado != null)
            estActual = newEstado;
        if (newPrioridad != null)
            prioActual = newPrioridad;

        // Seteamos las actualizaciones.

        tarea.setTitulo(tituloActual);
        tarea.setDescripcion(descActual);
        tarea.setFecha_inicio(fecha_inicio_act);
        tarea.setFecha_vencimiento(fecha_vencimiento_act);
        tarea.setEstado(estActual);
        tarea.setPrioridad(prioActual);

        // csv
        List<Tarea> lista = new ArrayList<>(map.values());
        csvManager.guardarTareas(lista);
        System.out.println("Tarea modificada con exito ✅. ID: " + id);
    }

    public void listarEstado() {
        System.out.println("Pedientes");
        for (Tarea t : map.values()) {
            if (t.getEstado() == Estado.PENDIENTE) {
                System.out.println("ID: " + t.getId() + " - " + t.getTitulo());
            }
        }

        System.out.println("En progreso: ");
        for (Tarea t : map.values()) {
            if (t.getEstado() == Estado.EN_PROGRESO) {
                System.out.println("ID: " + t.getId() + " - " + t.getTitulo());
            }
        }

        System.out.println("Completada: ");
        for (Tarea t : map.values()) {
            if (t.getEstado() == Estado.COMPLETADA) {
                System.out.println("ID: " + t.getId() + " - " + t.getTitulo());
            }
        }
    }

    public void listarVencenSemanaActual() {

        LocalDate hoy = LocalDate.now();
        LocalDate lunes = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1); // resto el valor del dia -1 y eso me da los
                                                                            // dias que faltan para el lunes.
        LocalDate domingo = lunes.plusDays(6);

        System.out.println("Las tareas que vencen esta semana de " + lunes + " a " + domingo);

        for (Tarea t : map.values()) {
            LocalDate fecha_vencimiento_act = t.getFecha_vencimiento();
            if (fecha_vencimiento_act != null && !fecha_vencimiento_act.isBefore(lunes)
                    && !fecha_vencimiento_act.isAfter(domingo)) {
                System.out.println("ID: " + t.getId() + " - " + t.getTitulo() + "Vence: " + fecha_vencimiento_act);
            }
        }
        // Si la fecha actual es distinta de nulo y si la fecha actual no es antes del
        // lunes y despues del domingo imprime le fecha.

    }
}