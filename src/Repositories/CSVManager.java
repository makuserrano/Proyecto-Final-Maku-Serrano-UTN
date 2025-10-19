package Repositories;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Enums.Estado;
import Enums.Prioridad;
import Model.Tarea;

public class CSVManager {

    private final String rutaArchivo;
    private static final String HEADER = "id,titulo,descripcion,fecha_inicio,fecha_vencimiento,estado,prioridad";

    public CSVManager(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public List<Tarea> leerTareas() {
        List<Tarea> tareas = new ArrayList<>();

        File file = new File(rutaArchivo);
        if (!file.exists()) {
            System.out.println("El archivo se creara al guardar.");
            return tareas;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea = br.readLine();

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty())
                    continue;

                String[] campos = linea.split(",");
                if (campos.length != 7)
                    continue;

                long id = Long.parseLong(campos[0].trim());
                String titulo = campos[1].replace("\"", "").trim();
                String descripcion = campos[2].replace("\"", "").trim();
                LocalDate fecha_inicio = LocalDate.parse(campos[3].trim());
                LocalDate fecha_vencimiento = LocalDate.parse(campos[4].trim());
                Estado estado = Estado.valueOf(campos[5].trim().toUpperCase());
                Prioridad prioridad = Prioridad.valueOf(campos[6].trim().toUpperCase());

                Tarea tarea = new Tarea(id, titulo, descripcion, fecha_inicio, fecha_vencimiento, estado, prioridad);
                tareas.add(tarea);
            }
        } catch (Exception e) {
            System.out.println("Error al leer el archivo" + e.getMessage());
        }
        return tareas;
    }
}