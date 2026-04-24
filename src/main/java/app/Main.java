package app;

//import dao.ActoresDAO;
//import dao.ActoresDAOImpl;
//import dao.PeliculasDAO;
//import dao.PeliculasDAOImpl;
//import modelo.Actores;
//import modelo.Peliculas;

//import dao.DirectorDAO;
//import dao.DirectorDAOImpl;
//import modelo.Director;

//import dao.EjemplarDAO;
//import dao.EjemplarDAOException;
//import dao.EjemplarDAOImpl;
//import modelo.Ejemplar;
//import modelo.EstadoConservacion;

import dao.SocioDAO;
import dao.SocioDAOException;
import dao.SocioDAOImpl;
import modelo.Socio;

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
    //private static ActoresDAO actoresDAO;
    //private static DirectorDAO directorDAO;
    //private static EjemplarDAO ejemplarDAO;
    private static SocioDAO socioDAO;

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/videoclub";
        String user = "root";
        String pass = "tu_password";

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            // Inicializamos el DAO
            // peliculasDAO = new PeliculasDAOImpl(connection);
            //actoresDAO = new ActoresDAOImpl(connection);
            //directorDAO = new DirectorDAOImpl(connection);
            //ejemplarDAO = new EjemplarDAOImpl(connection);
            socioDAO = new SocioDAOImpl(connection);

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
                    //case 2: menuActores();
                    //    break;
                    //case 3:
                    //    menuDirectores();
                    //    break;
                    //case 4:
                    //    menuEjemplares();
                    //    break;
                    case 5:
                        menuSocios();
                        break;
                    case 6:
                        menuAlquileres();
                        break;
                    case 7:
                        salirGeneral = true;
                        break;
                    default:
                        System.out.println("Opción no válida.");
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
                    System.out.println("Actor asociado a la película con éxito.");
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

    private static void menuDirectores() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n--- 3.3 GESTIÓN DE DIRECTORES ---");
            System.out.println("1. Registrar un nuevo director");
            System.out.println("2. Consultar directores");
            System.out.println("3. Asignar director a película");
            System.out.println("4. Modificar datos de director");
            System.out.println("5. Eliminar director");
            System.out.println("6. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            switch (leerOpcion()) {
                case 1: {
                    System.out.println("\n-> Ejecutando: Alta de nuevo director...");
                    System.out.print("Introduce el nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Introduce la nacionalidad: ");
                    String nacionalidad = sc.nextLine();

                    if (nombre.isBlank() || nacionalidad.isBlank()) {
                        System.out.println("Todos los campos son obligatorios.");
                        break;
                    }

                    Director nuevo = new Director(0, nombre, nacionalidad);
                    nuevo = directorDAO.darDeAlta(nuevo);
                    System.out.println("¡Director guardado con éxito! ID: " + nuevo.getId());
                    break;
                }
                case 2: {
                    System.out.println("\n-> Ejecutando: Consultar directores...");
                    System.out.println("1. Buscar por nombre");
                    System.out.println("2. Buscar por nacionalidad");
                    System.out.println("3. Listar todos");
                    System.out.print("Elija un filtro: ");
                    int filtro = leerOpcion();

                    List<Director> resultados = new ArrayList<>();
                    if (filtro == 1) {
                        System.out.print("Introduce el nombre a buscar: ");
                        resultados = directorDAO.buscarPorNombre(sc.nextLine());
                    } else if (filtro == 2) {
                        System.out.print("Introduce la nacionalidad a buscar: ");
                        resultados = directorDAO.buscarPorNacionalidad(sc.nextLine());
                    } else if (filtro == 3) {
                        resultados = directorDAO.listarTodos();
                    } else {
                        System.out.println("Filtro no válido.");
                    }

                    if (resultados.isEmpty()) {
                        System.out.println("No se encontraron directores.");
                    } else {
                        resultados.forEach(System.out::println);
                    }
                    break;
                }
                case 3: {
                    System.out.println("\n-> Ejecutando: Asignar director a película...");
                    System.out.print("Introduce el ID del director: ");
                    int idDirector = leerOpcion();
                    System.out.print("Introduce el ID de la película: ");
                    int idPelicula = leerOpcion();

                    directorDAO.asignarAPelicula(idDirector, idPelicula);
                    System.out.println("Director asignado a la película con éxito.");
                    break;
                }
                case 4: {
                    System.out.println("\n-> Ejecutando: Modificar datos...");
                    System.out.print("Introduce el ID del director a modificar: ");
                    int idMod = leerOpcion();

                    Optional<Director> directorOpt = directorDAO.buscarPorId(idMod);
                    if (directorOpt.isPresent()) {
                        Director directorMod = directorOpt.get();
                        System.out.println("Director actual: " + directorMod);

                        System.out.print("Nuevo nombre (deja en blanco para no cambiar): ");
                        String nNombre = sc.nextLine();
                        if (!nNombre.isBlank()) directorMod.setNombre(nNombre);

                        System.out.print("Nueva nacionalidad (deja en blanco para no cambiar): ");
                        String nNac = sc.nextLine();
                        if (!nNac.isBlank()) directorMod.setNacionalidad(nNac);

                        directorDAO.actualizar(directorMod);
                        System.out.println("¡Director actualizado con éxito!");
                    } else {
                        System.out.println("No existe ningún director con ese ID.");
                    }
                    break;
                }
                case 5: {
                    System.out.println("\n-> Ejecutando: Eliminar director...");
                    System.out.print("Introduce el ID del director a eliminar: ");
                    int idElim = leerOpcion();

                    Optional<Director> directorElim = directorDAO.buscarPorId(idElim);
                    if (directorElim.isPresent()) {
                        System.out.println("Director encontrado: " + directorElim.get());

                        if (directorDAO.eliminarPorId(idElim)) {
                            System.out.println("Director eliminado correctamente.");
                        } else {
                            System.out.println("No se pudo eliminar el director.");
                        }
                    } else {
                        System.out.println("No existe ningún director con ese ID.");
                    }
                    break;
                }
                case 6:
                    volver = true;
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void menuEjemplares() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n--- 3.4 GESTIÓN DE EJEMPLARES ---");
            System.out.println("1. Dar de alta un nuevo ejemplar");
            System.out.println("2. Consultar ejemplares de una película");
            System.out.println("3. Actualizar estado de conservación");
            System.out.println("4. Eliminar ejemplar");
            System.out.println("5. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            switch (leerOpcion()) {
                case 1: {
                    System.out.println("\n-> Ejecutando: Alta de nuevo ejemplar...");
                    System.out.print("Introduce el ID de la película: ");
                    int idPelicula = leerOpcion();

                    System.out.println("Selecciona el estado de conservación:");
                    EstadoConservacion[] estados = EstadoConservacion.values();
                    for (int i = 0; i < estados.length; i++) {
                        System.out.println((i + 1) + ". " + estados[i]);
                    }
                    System.out.print("Elige una opción: ");
                    int opEstado = leerOpcion();

                    if (opEstado < 1 || opEstado > estados.length) {
                        System.out.println("Estado no válido.");
                        break;
                    }

                    Ejemplar nuevo = new Ejemplar(0, idPelicula, estados[opEstado - 1]);
                    nuevo = ejemplarDAO.darDeAlta(nuevo);
                    System.out.println("¡Ejemplar dado de alta con éxito! Nº: " + nuevo.getNumEjemplar());
                    break;
                }
                case 2: {
                    System.out.println("\n-> Ejecutando: Consultar ejemplares...");
                    System.out.print("Introduce el ID de la película: ");
                    int idPelicula = leerOpcion();

                    List<Ejemplar> ejemplares = ejemplarDAO.buscarPorPelicula(idPelicula);
                    if (ejemplares.isEmpty()) {
                        System.out.println("No se encontraron ejemplares para esa película.");
                    } else {
                        ejemplares.forEach(System.out::println);
                    }
                    break;
                }
                case 3: {
                    System.out.println("\n-> Ejecutando: Actualizar estado de conservación...");
                    System.out.print("Introduce el número del ejemplar: ");
                    int numEjemplar = leerOpcion();

                    Optional<Ejemplar> ejemplarOpt = ejemplarDAO.buscarPorId(numEjemplar);
                    if (ejemplarOpt.isPresent()) {
                        System.out.println("Ejemplar actual: " + ejemplarOpt.get());

                        System.out.println("Selecciona el nuevo estado:");
                        EstadoConservacion[] estados = EstadoConservacion.values();
                        for (int i = 0; i < estados.length; i++) {
                            System.out.println((i + 1) + ". " + estados[i]);
                        }
                        System.out.print("Elige una opción: ");
                        int opEstado = leerOpcion();

                        if (opEstado < 1 || opEstado > estados.length) {
                            System.out.println("Estado no válido.");
                            break;
                        }

                        ejemplarDAO.actualizarEstado(numEjemplar, estados[opEstado - 1]);
                        System.out.println("¡Estado actualizado con éxito!");
                    } else {
                        System.out.println("No existe ningún ejemplar con ese número.");
                    }
                    break;
                }
                case 4: {
                    System.out.println("\n-> Ejecutando: Eliminar ejemplar...");
                    System.out.print("Introduce el número del ejemplar a eliminar: ");
                    int numElim = leerOpcion();

                    Optional<Ejemplar> ejemplarElim = ejemplarDAO.buscarPorId(numElim);
                    if (ejemplarElim.isPresent()) {
                        System.out.println("Ejemplar encontrado: " + ejemplarElim.get());
                        try {
                            if (ejemplarDAO.eliminarPorId(numElim)) {
                                System.out.println("Ejemplar eliminado correctamente.");
                            } else {
                                System.out.println("No se pudo eliminar el ejemplar.");
                            }
                        } catch (EjemplarDAOException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("No existe ningún ejemplar con ese número.");
                    }
                    break;
                }
                case 5:
                    volver = true;
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

     */

    private static void menuSocios() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n--- 3.5 GESTIÓN DE SOCIOS ---");
            System.out.println("1. Registrar un nuevo socio");
            System.out.println("2. Consultar socios");
            System.out.println("3. Modificar datos de un socio");
            System.out.println("4. Eliminar un socio");
            System.out.println("5. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            switch (leerOpcion()) {
                case 1: {
                    System.out.println("\n-> Ejecutando: Registrar nuevo socio...");
                    System.out.print("Introduce el DNI: ");
                    String dni = sc.nextLine();
                    System.out.print("Introduce el nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Introduce la dirección: ");
                    String direccion = sc.nextLine();
                    System.out.print("Introduce el teléfono: ");
                    String telefono = sc.nextLine();
                    System.out.print("Introduce el DNI del socio aval (o el mismo DNI si es el primero): ");
                    String dniAval = sc.nextLine();

                    if (dni.isBlank() || nombre.isBlank() || direccion.isBlank() || telefono.isBlank() || dniAval.isBlank()) {
                        System.out.println("Todos los campos son obligatorios.");
                        break;
                    }

                    // Comprobamos que el socio aval existe, salvo que se avale a sí mismo
                    if (!dniAval.equals(dni)) {
                        Optional<Socio> avalOpt = socioDAO.buscarPorDni(dniAval);
                        if (avalOpt.isEmpty()) {
                            System.out.println("Error: El socio aval con DNI " + dniAval + " no existe.");
                            break;
                        }
                    }

                    Socio nuevo = new Socio(dni, nombre, direccion, telefono, dniAval);
                    socioDAO.darDeAlta(nuevo);
                    System.out.println("¡Socio registrado con éxito! DNI: " + nuevo.getDni());
                    break;
                }
                case 2: {
                    System.out.println("\n-> Ejecutando: Consultar socios...");
                    System.out.println("1. Buscar por DNI");
                    System.out.println("2. Buscar por nombre");
                    System.out.println("3. Buscar por teléfono");
                    System.out.println("4. Listar todos");
                    System.out.print("Elija un filtro: ");
                    int filtro = leerOpcion();

                    if (filtro == 1) {
                        System.out.print("Introduce el DNI: ");
                        Optional<Socio> resultado = socioDAO.buscarPorDni(sc.nextLine());
                        if (resultado.isPresent()) {
                            System.out.println(resultado.get());
                        } else {
                            System.out.println("No se encontró ningún socio con ese DNI.");
                        }
                    } else {
                        List<Socio> resultados = new ArrayList<>();
                        if (filtro == 2) {
                            System.out.print("Introduce el nombre: ");
                            resultados = socioDAO.buscarPorNombre(sc.nextLine());
                        } else if (filtro == 3) {
                            System.out.print("Introduce el teléfono: ");
                            resultados = socioDAO.buscarPorTelefono(sc.nextLine());
                        } else if (filtro == 4) {
                            resultados = socioDAO.listarTodos();
                        } else {
                            System.out.println("Filtro no válido.");
                            break;
                        }

                        if (resultados.isEmpty()) {
                            System.out.println("No se encontraron socios.");
                        } else {
                            resultados.forEach(System.out::println);
                        }
                    }
                    break;
                }
                case 3: {
                    System.out.println("\n-> Ejecutando: Modificar datos de socio...");
                    System.out.print("Introduce el DNI del socio a modificar: ");
                    String dniMod = sc.nextLine();

                    Optional<Socio> socioOpt = socioDAO.buscarPorDni(dniMod);
                    if (socioOpt.isPresent()) {
                        Socio socioMod = socioOpt.get();
                        System.out.println("Socio actual: " + socioMod);

                        System.out.print("Nuevo nombre (deja en blanco para no cambiar): ");
                        String nNombre = sc.nextLine();
                        if (!nNombre.isBlank()) socioMod.setNombre(nNombre);

                        System.out.print("Nueva dirección (deja en blanco para no cambiar): ");
                        String nDireccion = sc.nextLine();
                        if (!nDireccion.isBlank()) socioMod.setDireccion(nDireccion);

                        System.out.print("Nuevo teléfono (deja en blanco para no cambiar): ");
                        String nTelefono = sc.nextLine();
                        if (!nTelefono.isBlank()) socioMod.setTelefono(nTelefono);

                        System.out.print("Nuevo DNI aval (deja en blanco para no cambiar): ");
                        String nDniAval = sc.nextLine();
                        if (!nDniAval.isBlank()) {
                            // Comprobamos que el nuevo aval existe
                            if (!nDniAval.equals(dniMod) && socioDAO.buscarPorDni(nDniAval).isEmpty()) {
                                System.out.println("Error: El socio aval con DNI " + nDniAval + " no existe.");
                                break;
                            }
                            socioMod.setDniAval(nDniAval);
                        }

                        socioDAO.actualizar(socioMod);
                        System.out.println("¡Socio actualizado con éxito!");
                    } else {
                        System.out.println("No existe ningún socio con ese DNI.");
                    }
                    break;
                }
                case 4: {
                    System.out.println("\n-> Ejecutando: Eliminar socio...");
                    System.out.print("Introduce el DNI del socio a eliminar: ");
                    String dniElim = sc.nextLine();

                    Optional<Socio> socioElim = socioDAO.buscarPorDni(dniElim);
                    if (socioElim.isPresent()) {
                        System.out.println("Socio encontrado: " + socioElim.get());
                        try {
                            if (socioDAO.eliminarPorDni(dniElim)) {
                                System.out.println("Socio eliminado correctamente.");
                            } else {
                                System.out.println("No se pudo eliminar el socio.");
                            }
                        } catch (SocioDAOException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("No existe ningún socio con ese DNI.");
                    }
                    break;
                }
                case 5:
                    volver = true;
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    // Métodos vacíos para los otros módulos (Alquileres)
    private static void menuAlquileres() {
        System.out.println("Módulo de Alquileres en desarrollo...");
    }
}


