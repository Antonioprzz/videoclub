package app;

import dao.PeliculasDAO;
import dao.PeliculasDAOImpl;
import modelo.Peliculas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static PeliculasDAO peliculasDAO;

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/videoclub";
        String user = "root";
        String pass = "tu_password";

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            // Inicializamos el DAO
            peliculasDAO = new PeliculasDAOImpl(connection);

            boolean salirGeneral = false;
            int opcion;

            while (!salirGeneral) {
                System.out.println("=======================================");
                System.out.println("   SISTEMA DE GESTIÓN DE VIDEOCLUB");
                System.out.println("=======================================");
                System.out.println("1. Gestión de Películas");
                System.out.println("2. Gestión de Actores");
                System.out.println("3. Gestión de Directores");
                System.out.println("4. Gestión de Ejemplares");
                System.out.println("5. Gestión de Socios");
                System.out.println("6. Gestión de Alquileres");
                System.out.println("7. Salir del Programa");
                System.out.print("Seleccione un módulo: ");

                opcion = leerOpcion();

                switch (opcion) {
                    case 1: menuPeliculas();
                        break;
                    case 2: menuActores();
                        break;
                    case 3: menuDirectores();
                        break;
                    case 4: menuEjemplares();
                        break;
                    case 5: menuSocios();
                        break;
                    case 6: menuAlquileres();
                        break;
                    case 7: salirGeneral = true;
                        break;
                    default: System.out.println("Opción no válida.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        System.out.println("Programa finalizado. ¡Hasta pronto!");
    }

    private static int leerOpcion() {
        while (!sc.hasNextInt()) {
            System.out.print("Por favor, introduce un número válido: ");
            sc.next();
        }
        int opt = sc.nextInt();
        sc.nextLine(); // Limpiamos el buffer
        return opt;
    }

    private static void menuPeliculas() {
        boolean volver = false;

        while (!volver) {
            System.out.println("--- 3.1 GESTIÓN DE PELÍCULAS ---");
            System.out.println("1. Alta de nueva película");
            System.out.println("2. Consultar y buscar (Título/Nac./Prod.)");
            System.out.println("3. Modificar datos");
            System.out.println("4. Eliminar película");
            System.out.println("5. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            switch (leerOpcion()) {
                case 1:
                    System.out.println("-> Ejecutando: Alta de nueva película...");
                    System.out.print("Introduce el título: ");
                    String titulo = sc.nextLine();
                    System.out.print("Introduce la nacionalidad: ");
                    String nacionalidad = sc.nextLine();
                    System.out.print("Introduce la productora: ");
                    String productora = sc.nextLine();
                    System.out.print("Introduce la fecha (AAAA-MM-DD): ");
                    LocalDate fecha = LocalDate.parse(sc.nextLine());
                    System.out.print("Introduce el ID del director: ");
                    int idDirector = leerOpcion();

                    Peliculas nueva = new Peliculas(0, titulo, nacionalidad, productora, fecha, idDirector);
                    peliculasDAO.darDeAlta(nueva);
                    System.out.println("¡Película guardada con éxito! ID: " + nueva.getId());
                    break;

                case 2:
                    System.out.println("--- CONSULTA Y BÚSQUEDA ---");
                    System.out.println("1. Buscar por Título");
                    System.out.println("2. Buscar por Nacionalidad");
                    System.out.println("3. Buscar por Productora");
                    System.out.println("4. Listar todas");
                    System.out.print("Seleccione: ");
                    int sub = leerOpcion();
                    List<Peliculas> lista = new ArrayList<>();

                    if (sub == 1) {
                        System.out.print("Título: ");
                        lista = peliculasDAO.buscarPorTitulo(sc.nextLine());
                    } else if (sub == 2) {
                        System.out.print("Nacionalidad: ");
                        lista = peliculasDAO.buscarPorNacionalidad(sc.nextLine());
                    } else if (sub == 3) {
                        System.out.print("Productora: ");
                        lista = peliculasDAO.buscarPorProductora(sc.nextLine());
                    } else if (sub == 4) {
                        lista = peliculasDAO.listarTodas();
                    }

                    if (lista.isEmpty()) {
                        System.out.println("No se han encontrado resultados.");
                    } else {
                        lista.forEach(System.out::println);
                    }
                    break;

                case 3:
                    System.out.print("ID de la película a modificar: ");
                    int idMod = leerOpcion();
                    Optional<Peliculas> optPeli = peliculasDAO.buscarPorId(idMod);

                    if (optPeli.isPresent()) {
                        Peliculas p = optPeli.get();
                        System.out.println("Modificando: " + p.getTitulo());

                        System.out.print("Nuevo título: ");
                        String t = sc.nextLine();
                        if (!t.isEmpty()) p.setTitulo(t);

                        System.out.print("Nueva nacionalidad: ");
                        String n = sc.nextLine();
                        if (!n.isEmpty()) p.setNacionalidad(n);

                        System.out.print("Nueva productora: ");
                        String pr = sc.nextLine();
                        if (!pr.isEmpty()) p.setProductora(pr);

                        System.out.print("Nuevo ID Director (0 para omitir): ");
                        int idD = leerOpcion();
                        if (idD != 0) p.setIdDirector(idD);

                        peliculasDAO.actualizar(p);
                        System.out.println("Película actualizada con éxito.");
                    } else {
                        System.out.println("Error: No existe película con ID " + idMod);
                    }
                    break;

                case 4:
                    System.out.print("ID de la película a eliminar: ");
                    int idEli = leerOpcion();
                    System.out.print("¿Seguro que desea eliminar? (Si/No): ");
                    if (sc.nextLine().equals("Si")) {
                        if (peliculasDAO.eliminarPorId(idEli)) {
                            System.out.println("Película eliminada.");
                        } else {
                            System.out.println("No se pudo eliminar (ID no encontrado).");
                        }
                    }
                    break;

                case 5:
                    volver = true;
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    // Métodos vacíos para los otros módulos (Actores, Directores, Ejemplares, Socios, Alquileres)
    private static void menuActores() {
        System.out.println("Módulo de Actores en desarrollo...");
    }

    private static void menuDirectores() {
        System.out.println("Módulo de Directores en desarrollo...");
    }
    private static void menuEjemplares() {
        System.out.println("Módulo de Ejemplares en desarrollo...");
    }
    private static void menuSocios() {
        System.out.println("Módulo de Socios en desarrollo...");
    }
    private static void menuAlquileres() {
        System.out.println("Módulo de Alquileres en desarrollo...");
    }
}
