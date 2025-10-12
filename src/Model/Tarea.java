package Model;

import java.time.LocalDate;

import Enums.Estado;
import Enums.Prioridad;

public class Tarea {

    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDate fecha_inicio;
    private LocalDate fecha_vencimiento;
    private Estado estado;
    private Prioridad prioridad;

    public Tarea(Long id, String titulo, String descripcion, LocalDate fecha_inicio, LocalDate fecha_vencimiento,
            Estado estado, Prioridad prioridad) {

        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio");

        }

        if (fecha_inicio == null || fecha_vencimiento == null) {
            throw new IllegalArgumentException("La fechas no pueden ser nulas");

        }

        if (fecha_vencimiento.isBefore(fecha_inicio)) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la fecha de creacion");
        }

        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_inicio = fecha_inicio;
        this.fecha_vencimiento = fecha_vencimiento;
        this.estado = estado;
        this.prioridad = prioridad;
    }

    @Override
    public String toString() {
        return id + ", " + titulo + ", " + descripcion + ", " + estado + ", " + prioridad + ", " + fecha_inicio + ", "
                + fecha_vencimiento;
    }
}
