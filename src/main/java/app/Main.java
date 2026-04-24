package app;

import dao.ActoresDAO;
import dao.ActoresDAOImpl;
//import dao.PeliculasDAO;
//import dao.PeliculasDAOImpl;
import modelo.Actores;
//import modelo.Peliculas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    //private static PeliculasDAO peliculasDAO;
    private static ActoresDAO actoresDAO;

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/videoclub";
        String user = "root";
        String pass = "tu_password";

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            // Inicializamos el DAO
            // peliculasDAO = new PeliculasDAOImpl(connection);
            actoresDAO = new ActoresDAOImpl(connection);

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
                    // case 1: menuPeliculas();
                        // break;
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
            System.out.println("Error al conectar [" + e.getErrorCode() + "]: " + e.getMessage());
        }
        System.out.println("Programa finalizado. ¡Hasta pronto!");
        sc.close();
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
/*
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
*/
private static void menuActores() {
    boolean volver = false;

    while (!volver) {
        System.out.println("\n--- 3.2 GESTIÓN DE ACTORES ---");
        System.out.println("1. Registrar un nuevo actor");
        System.out.println("2. Consultar actores (Filtrar)");
        System.out.println("3. Asociar un actor a película");
        System.out.println("4. Modificar datos de actor");
        System.out.println("5. Eliminar o desvincular actor");
        System.out.println("6. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        switch (leerOpcion()) {
            case 1:
                System.out.println("\n-> Ejecutando: Alta de nuevo actor...");
                System.out.print("Introduce el nombre: ");
                String nombre = sc.nextLine();
                System.out.print("Introduce la nacionalidad: ");
                String nacionalidad = sc.nextLine();
                System.out.print("Introduce el sexo: ");
                String sexo = sc.nextLine();

                if (nombre.isBlank() || nacionalidad.isBlank() || sexo.isBlank()) {
                    System.out.println("Todos los campos son obligatorios.");
                    break;
                }

                Actores nuevo = new Actores(0, nombre, nacionalidad, sexo);
                nuevo = actoresDAO.darDeAlta(nuevo);
                System.out.println("¡Actor guardado con éxito! ID: " + nuevo.getId());
                break;

            case 2:
                System.out.println("\n-> Ejecutando: Consultar actores...");
                System.out.println("1. Buscar por nombre");
                System.out.println("2. Buscar por nacionalidad");
                System.out.println("3. Listar todos");
                System.out.print("Elija un filtro: ");
                int filtro = leerOpcion();

                List<Actores> resultados = new ArrayList<>();
                if (filtro == 1) {
                    System.out.print("Introduce el nombre a buscar: ");
                    resultados = actoresDAO.buscarPorNombre(sc.nextLine());
                } else if (filtro == 2) {
                    System.out.print("Introduce la nacionalidad a buscar: ");
                    resultados = actoresDAO.buscarPorNacionalidad(sc.nextLine());
                } else if (filtro == 3) {
                    resultados = actoresDAO.listarTodos();
                } else {
                    System.out.println("Filtro no válido.");
                }

                if (resultados != null) {
                    if (resultados.isEmpty()) {
                        System.out.println("No se encontraron actores.");
                    } else {
                        resultados.forEach(System.out::println);
                    }
                }
                break;

            case 3:
                System.out.println("\n-> Ejecutando: Asociar actor a película...");
                System.out.print("Introduce el ID del actor: ");
                int idActorAsoc = Integer.parseInt(sc.nextLine());
                System.out.print("Introduce el ID de la película: ");
                int idPeliculaAsoc = Integer.parseInt(sc.nextLine());
                System.out.print("Introduce el rol del actor en la película (ej. Protagonista, Extra): ");
                String rol = sc.nextLine();

                actoresDAO.asociarAPelicula(idActorAsoc, idPeliculaAsoc, rol);
                System.out.println("Actor asociado a la película con éxito. (Requiere método en DAO)");
                break;

            case 4:
                System.out.println("\n-> Ejecutando: Modificar datos...");
                System.out.print("Introduce el ID del actor a modificar: ");
                int idMod = leerOpcion();

                Optional<Actores> actorOpt = actoresDAO.buscarPorId(idMod);
                if (actorOpt.isPresent()) {
                    Actores actorMod = actorOpt.get();
                    System.out.println("Actor actual: " + actorMod);

                    System.out.print("Nuevo nombre (deja en blanco para no cambiar): ");
                    String nNombre = sc.nextLine();
                    if (!nNombre.isBlank()) actorMod.setNombre(nNombre);

                    System.out.print("Nueva nacionalidad (deja en blanco para no cambiar): ");
                    String nNac = sc.nextLine();
                    if (!nNac.isBlank()) actorMod.setNacionalidad(nNac);

                    System.out.print("Nuevo sexo (deja en blanco para no cambiar): ");
                    String nSexo = sc.nextLine();
                    if (!nSexo.isBlank()) actorMod.setSexo(nSexo);

                    actoresDAO.actualizar(actorMod);
                    System.out.println("¡Actor actualizado con éxito!");
                } else {
                    System.out.println("No existe ningún actor con ese ID.");
                }
                break;

            case 5:
                System.out.println("\n-> Ejecutando: Eliminar o desvincular actor...");
                System.out.println("1. Eliminar actor del sistema");
                System.out.println("2. Desvincular actor de una película");
                System.out.print("Elija una opción: ");
                int opcEliminar = leerOpcion();

                if (opcEliminar == 1) {
                    System.out.print("Introduce el ID del actor a eliminar: ");
                    int idElim = Integer.parseInt(sc.nextLine());
                    boolean eliminado = actoresDAO.eliminarPorId(idElim);
                    if (eliminado) System.out.println("Actor eliminado correctamente.");
                    else System.out.println("No se pudo eliminar (o no existe).");
                } else if (opcEliminar == 2) {
                    System.out.print("Introduce el ID del actor: ");
                    int idActDesv = Integer.parseInt(sc.nextLine());
                    System.out.print("Introduce el ID de la película: ");
                    int idPelDesv = Integer.parseInt(sc.nextLine());

                    actoresDAO.desvincularDePelicula(idActDesv, idPelDesv);
                    System.out.println("Actor desvinculado de la película.");
                } else {
                    System.out.println("Opción no válida.");
                }
                break;

            case 6:
                volver = true;
                break;

            default:
                System.out.println("Opción no válida.");
        }
    }
}

    // Métodos vacíos para los otros módulos (Directores, Ejemplares, Socios, Alquileres)
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
