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
            System.out.println("ID: " + t.getId() + " - " + t.getTitulo());
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

        //ahora si.
        try{
            Tarea nuevaTarea = new Tarea(nextId,titulo.trim(), descripcion.trim(), fecha_inicio, fecha_vencimiento, estado, prioridad);

                map.put(nuevaTarea.getId(), nuevaTarea);
                nextId++;

                //guardamos

                List<Tarea> lista = new ArrayList<>(map.values());
                csvManager.guardarTareas(lista);
                System.out.println("Tarea creada ✅. ID: " + nuevaTarea.getId());

        } catch(Exception e){
            System.out.println("Error al crear tarea" + e.getMessage());
        }
    }
}