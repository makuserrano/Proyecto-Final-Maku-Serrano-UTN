package App;

import java.util.Scanner;

import Enums.Estado;
import Enums.Prioridad;
import Repositories.CSVManager;
import Service.GestorTarea;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        CSVManager csv = new CSVManager("src/data/tareas.csv");
        GestorTarea gestor = new GestorTarea(csv);
        gestor.cargarTareas();
        int opt = 0;
        do {
            System.out.println("\n===== MENÚ PRINCIPAL =====");
            System.out.println("1) Crear tarea");
            System.out.println("2) Listar todas las tareas");
            System.out.println("3) Editar tarea");
            System.out.println("4) Eliminar tarea");
            System.out.println("5) Reportes");
            System.out.println("6) Salir");
            System.out.print("Elija una opción: ");
            // manejo de errores si el usuario pone texto no explote.
            try {
                opt = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
                continue;
            }

            switch (opt) {
                case 1:
                    System.out.print("Título: ");
                    String titulo = sc.nextLine();
                    System.out.print("Descripción: ");
                    String descripcion = sc.nextLine();
                    System.out.print("Fecha de inicio (YYYY-MM-DD): ");
                    String fecha_inicio = sc.nextLine();
                    System.out.print("Fecha de vencimiento (YYYY-MM-DD): ");
                    String fecha_vencimieto = sc.nextLine();
                    System.out.print("Estado (PENDIENTE, EN_PROGRESO, COMPLETADA): ");
                    Estado estado = Estado.valueOf(sc.nextLine().trim().toUpperCase());
                    System.out.print("Prioridad (ALTA, MEDIA, BAJA): ");
                    Prioridad prioridad = Prioridad.valueOf(sc.nextLine().trim().toUpperCase());

                    gestor.crearTarea(titulo, descripcion, fecha_inicio, fecha_vencimieto, estado, prioridad);
                    break;

                case 2:
                    gestor.listarTareas();
                    break;

                case 3:
                    System.out.print("ID de la tarea a editar: ");
                    long newId = Long.parseLong(sc.nextLine());
                    System.out.print("Nuevo título: ");
                    String newTitulo = sc.nextLine();
                    System.out.print("Nueva descripción: ");
                    String newDesc = sc.nextLine();
                    System.out.print("Nueva fecha inicio (YYYY-MM-DD): ");
                    String newFecha_inicio = sc.nextLine();
                    System.out.print("Nueva fecha vencimiento (YYYY-MM-DD): ");
                    String newFecha_vencimiento = sc.nextLine();
                    System.out.print("Nuevo estado (PENDIENTE, EN_PROGRESO, COMPLETADA): ");
                    String newEstadoStr = sc.nextLine();
                    Estado newEstado = newEstadoStr.isEmpty() ? null
                            : Estado.valueOf(newEstadoStr.trim().toUpperCase());
                    System.out.print("Nueva prioridad (ALTA, MEDIA, BAJA): ");
                    String newPrioridadStr = sc.nextLine();
                    Prioridad newPrioridad = newPrioridadStr.isEmpty() ? null
                            : Prioridad.valueOf(newPrioridadStr.trim().toUpperCase());

                    gestor.editarTarea(newId, newTitulo, newDesc, newFecha_inicio, newFecha_vencimiento, newEstado,
                            newPrioridad);
                    break;

                case 4:
                    System.out.print("ID de la tarea a eliminar: ");
                    long idBorrado = Long.parseLong(sc.nextLine());
                    gestor.eliminarTarea(idBorrado);
                    break;
                case 5:
                    System.out.println("--- REPORTES ---");
                    System.out.println("1) Tareas por estado");
                    System.out.println("2) Tareas que vencen esta semana");
                    System.out.print("Elija: ");
                    int rep = Integer.parseInt(sc.nextLine());
                    if (rep == 1)
                        gestor.listarEstado();
                    else if (rep == 2)
                        gestor.listarVencenSemanaActual();
                    else
                        System.out.println("Opción inválida.");
                    break;

                case 6:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    break;
            }
        } while (opt != 6);
        sc.close();
    }
}
