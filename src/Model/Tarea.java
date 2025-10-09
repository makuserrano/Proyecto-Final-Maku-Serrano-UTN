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
}
