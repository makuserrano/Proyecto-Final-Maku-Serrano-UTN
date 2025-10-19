package Repositories;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

                try {
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

                    Tarea tarea = new Tarea(id, titulo, descripcion, fecha_inicio, fecha_vencimiento, estado,
                            prioridad);
                    tareas.add(tarea);
                } catch (Exception e) {
                    System.out.println("Se ignoro una linea que fue creada incorrectamente " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Error al leer el archivo " + e.getMessage());
        }
        return tareas;
    }

    public void guardarTareas(List<Tarea> tareas) {

        File file = new File(rutaArchivo);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            bw.write(HEADER);
            bw.newLine();

            for (Tarea t : tareas) {
                String id = String.valueOf(t.getId());
                String titulo = "\"" + (t.getTitulo() == null ? "" : t.getTitulo()) + "\"";
                String descripcion = "\"" + (t.getDescripcion() == null ? "" : t.getDescripcion()) + "\"";
                String fecha_inicio = t.getFecha_inicio() == null ? "" : t.getFecha_inicio().toString();
                String fecha_vencimiento = t.getFecha_vencimiento() == null ? "" : t.getFecha_vencimiento().toString();
                String estado = t.getEstado() == null ? "" : t.getEstado().toString().toLowerCase();
                String prioridad = t.getPrioridad() == null ? "" : t.getPrioridad().toString().toLowerCase();

                String linea = String.join(",", id, titulo, descripcion, fecha_inicio, fecha_vencimiento, estado,
                        prioridad);
                bw.write(linea);
                bw.newLine();
            }
            System.out.println("Archivo y tarea guardada correctamente.");
        } catch (Exception e) {
            System.out.println("La tarea no se pudo guardar " + e.getMessage());
        }

    }

}
