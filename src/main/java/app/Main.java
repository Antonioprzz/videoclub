package app;

import dao.*;
import dao.ActorDAO;
import dao.ActorDAOImpl;
import dao.PeliculaDAO;
import dao.PeliculaDAOImpl;
import modelo.Actor;
import modelo.Pelicula;

import dao.DirectorDAO;
import dao.DirectorDAOImpl;
import modelo.Director;

import dao.EjemplarDAO;
import dao.EjemplarDAOException;
import dao.EjemplarDAOImpl;
import modelo.Ejemplar;
import modelo.EstadoConservacion;

import dao.SocioDAO;
import dao.SocioDAOException;
import dao.SocioDAOImpl;
import modelo.Socio;

import modelo.Alquiler;

import conexionBD.ConexionBD;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Clase principal del Sistema de Gestión de Videoclub.
 *
 * Proporciona una interfaz de consola interactiva para gestionar las distintas
 * entidades del videoclub: películas, actores, directores, ejemplares, socios
 * y alquileres. Se conecta a una base de datos MySQL y delega las operaciones
 * de persistencia en los correspondientes objetos DAO.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 */
public class Main {

    /**
     * Scanner compartido para la lectura de entrada del usuario por consola.
     */
    private static final Scanner sc = new Scanner(System.in);

    /**
     * DAO para operaciones CRUD sobre películas.
     */
    private static PeliculaDAO peliculasDAO;

    /**
     * DAO para operaciones CRUD sobre actores.
     */
    private static ActorDAO actoresDAO;

    /**
     * DAO para operaciones CRUD sobre directores.
     */
    private static DirectorDAO directorDAO;

    /**
     * DAO para operaciones CRUD sobre ejemplares.
     */
    private static EjemplarDAO ejemplarDAO;

    /**
     * DAO para operaciones CRUD sobre socios.
     */
    private static SocioDAO socioDAO;

    /**
     * DAO para operaciones CRUD sobre alquileres.
     */
    private static AlquilerDAO alquilerDAO;

    // =========================================================
    //  MÉTODOS DE VALIDACIÓN
    // =========================================================

    /**
     * Letras válidas para el dígito de control del DNI español.
     */
    private static final String LETRAS_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";

    /**
     * Valida que un DNI español tenga el formato correcto (8 dígitos + letra)
     * y que la letra de control sea la correspondiente al número.
     *
     * @param dni cadena a validar
     * @return {@code true} si el DNI es válido; {@code false} en caso contrario
     */
    private static boolean validarDni(String dni) {
        if (dni == null) return false;
        String dniUpper = dni.trim().toUpperCase();
        return dniUpper.matches("\\d{8}[A-Z]");
    }

    /**
     * Valida que un número de teléfono español sea correcto.
     * Acepta números de 9 dígitos que comiencen por 6, 7, 8 o 9
     * y opcionalmente el prefijo internacional +34.
     *
     * @param telefono cadena a validar
     * @return {@code true} si el teléfono es válido; {@code false} en caso contrario
     */
    private static boolean validarTelefono(String telefono) {
        if (telefono == null) return false;
        String t = telefono.trim().replaceAll("[ \\-]", "");
        return t.matches("(\\+34)?[6789]\\d{8}");
    }

    /**
     * Valida que una cadena de texto no esté vacía y solo contenga
     * letras (incluyendo acentos, ñ, etc.) y espacios.
     *
     * @param texto cadena a validar
     * @return {@code true} si el texto es un nombre válido; {@code false} en caso contrario
     */
    private static boolean validarNombre(String texto) {
        if (texto == null || texto.isBlank()) return false;
        return texto.trim().matches("[\\p{L} .'-]+");
    }

    /**
     * Valida que una cadena no esté vacía y tenga una longitud mínima de 2
     * caracteres, sin restricción de caracteres (para direcciones, títulos, etc.).
     *
     * @param texto cadena a validar
     * @return {@code true} si el texto es válido; {@code false} en caso contrario
     */
    private static boolean validarTextoGeneral(String texto) {
        return texto != null && texto.trim().length() >= 2;
    }

    /**
     * Intenta parsear una fecha en formato AAAA-MM-DD.
     *
     * @param fechaStr cadena con la fecha
     * @return el {@link LocalDate} correspondiente, o {@code null} si el formato es inválido
     */
    private static LocalDate parsearFecha(String fechaStr) {
        try {
            return LocalDate.parse(fechaStr.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Solicita al usuario un DNI hasta que introduce uno válido.
     *
     * @param mensaje mensaje a mostrar antes de pedir el dato
     * @return DNI válido en mayúsculas
     */
    private static String leerDni(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String dni = sc.nextLine().trim().toUpperCase();
            if (validarDni(dni)) return dni;
            System.out.println("  ✗ DNI no válido. Debe tener 8 dígitos seguidos de la letra de control correcta (ej: 12345678Z).");
        }
    }

    /**
     * Solicita al usuario un teléfono hasta que introduce uno válido.
     *
     * @param mensaje mensaje a mostrar antes de pedir el dato
     * @return teléfono válido
     */
    private static String leerTelefono(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String telefono = sc.nextLine().trim();
            if (validarTelefono(telefono)) return telefono;
            System.out.println("  ✗ Teléfono no válido. Introduce 9 dígitos comenzando por 6, 7, 8 o 9 (ej: 612345678).");
        }
    }

    /**
     * Solicita al usuario un nombre (persona/lugar) hasta que introduce uno válido.
     *
     * @param mensaje mensaje a mostrar antes de pedir el dato
     * @return nombre válido
     */
    private static String leerNombre(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String nombre = sc.nextLine().trim();
            if (validarNombre(nombre)) return nombre;
            System.out.println("  ✗ Nombre no válido. Solo se permiten letras y espacios, mínimo 2 caracteres.");
        }
    }

    /**
     * Solicita al usuario un texto genérico (título, dirección, etc.) hasta que
     * introduce uno con al menos 2 caracteres.
     *
     * @param mensaje mensaje a mostrar antes de pedir el dato
     * @return texto válido
     */
    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();
            if (validarTextoGeneral(texto)) return texto;
            System.out.println("  ✗ El campo no puede estar vacío y debe tener al menos 2 caracteres.");
        }
    }

    /**
     * Solicita al usuario una fecha en formato AAAA-MM-DD hasta que introduce
     * una válida.
     *
     * @param mensaje mensaje a mostrar antes de pedir el dato
     * @return fecha válida
     */
    private static LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String fechaStr = sc.nextLine().trim();
            LocalDate fecha = parsearFecha(fechaStr);
            if (fecha != null) return fecha;
            System.out.println("  ✗ Fecha no válida. Usa el formato AAAA-MM-DD (ej: 2024-03-15).");
        }
    }

    /**
     * Solicita al usuario una fecha opcional: si deja el campo en blanco
     * se devuelve el valor por defecto indicado.
     *
     * @param mensaje        mensaje a mostrar
     * @param valorPorDefecto valor a devolver si el usuario no introduce nada
     * @return fecha introducida o {@code valorPorDefecto}
     */
    private static LocalDate leerFechaOpcional(String mensaje, LocalDate valorPorDefecto) {
        while (true) {
            System.out.print(mensaje);
            String fechaStr = sc.nextLine().trim();
            if (fechaStr.isBlank()) return valorPorDefecto;
            LocalDate fecha = parsearFecha(fechaStr);
            if (fecha != null) return fecha;
            System.out.println("  ✗ Fecha no válida. Usa el formato AAAA-MM-DD (ej: 2024-03-15) o deja en blanco.");
        }
    }

    /**
     * Solicita al usuario un DNI opcional: si deja el campo en blanco se devuelve
     * {@code null}. Si escribe algo, debe ser un DNI válido.
     *
     * @param mensaje mensaje a mostrar
     * @return DNI válido o {@code null}
     */
    private static String leerDniOpcional(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String dni = sc.nextLine().trim();
            if (dni.isBlank()) return null;
            String dniUp = dni.toUpperCase();
            if (validarDni(dniUp)) return dniUp;
            System.out.println("  ✗ DNI no válido. Debe tener 8 dígitos seguidos de la letra correcta, o deja en blanco.");
        }
    }

    /**
     * Solicita al usuario un ID entero positivo hasta que introduce uno válido.
     *
     * @param mensaje mensaje a mostrar
     * @return entero positivo introducido
     */
    private static int leerIdPositivo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (sc.hasNextInt()) {
                int val = sc.nextInt();
                sc.nextLine();
                if (val > 0) return val;
                System.out.println("  ✗ El ID debe ser un número entero positivo.");
            } else {
                System.out.println("  ✗ Entrada no válida. Introduce un número entero.");
                sc.nextLine();
            }
        }
    }

    // =========================================================
    //  PUNTO DE ENTRADA
    // =========================================================

    /**
     * Punto de entrada de la aplicación.
     * Establece la conexión con la base de datos SQLite del videoclub,
     * inicializa todos los objetos DAO y presenta el menú principal
     * al usuario. El bucle principal se mantiene activo hasta que el
     * usuario selecciona la opción de salir.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        ConexionBD conexionBD = new ConexionBD();
        conexionBD.inicializarBaseDatos();
        Connection connection = conexionBD.obtenerConexion();

        // Inicializamos los DAOs
        peliculasDAO = new PeliculaDAOImpl(connection);
        actoresDAO = new ActorDAOImpl(connection);
        directorDAO = new DirectorDAOImpl(connection);
        ejemplarDAO = new EjemplarDAOImpl(connection);
        socioDAO = new SocioDAOImpl(connection);
        alquilerDAO = new AlquilerDAOImpl(connection);

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
                case 3:
                    menuDirectores();
                    break;
                case 4:
                    menuEjemplares();
                    break;
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

        conexionBD.cerrar();
        System.out.println("Programa finalizado. ¡Hasta pronto!");
        sc.close();
    }

    /**
     * Lee un número entero válido desde la entrada estándar.
     *
     * Solicita repetidamente al usuario hasta que se introduce un valor
     * numérico entero. Tras leerlo, limpia el buffer del {@link Scanner}.
     *
     * @return el número entero introducido por el usuario
     */
    private static int leerOpcion() {
        while (!sc.hasNextInt()) {
            System.out.print("Por favor, introduce un número válido: ");
            sc.next();
        }
        int opt = sc.nextInt();
        sc.nextLine(); // Limpiamos el buffer
        return opt;
    }

    // =========================================================
    //  MENÚ PELÍCULAS
    // =========================================================

    /**
     * Muestra y gestiona el submenú de gestión de películas.
     * Permite al usuario realizar las siguientes operaciones:
     * Alta de una nueva película
     * Consulta y búsqueda por título, nacionalidad o productora
     * Modificación de los datos de una película existente
     * Eliminación de una película por su ID
     * El bucle se mantiene activo hasta que el usuario elige volver al menú principal
     */
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
                case 1: {
                    System.out.println("-> Ejecutando: Alta de nueva película...");

                    String titulo = leerTexto("Introduce el título: ");
                    String nacionalidad = leerNombre("Introduce la nacionalidad: ");
                    String productora = leerTexto("Introduce la productora: ");
                    LocalDate fecha = leerFecha("Introduce la fecha (AAAA-MM-DD): ");
                    int idDirector = leerIdPositivo("Introduce el ID del director: ");

                    Pelicula nueva = new Pelicula(0, titulo, nacionalidad, productora, fecha, idDirector);
                    peliculasDAO.darDeAlta(nueva);
                    System.out.println("¡Película guardada con éxito! ID: " + nueva.getId());
                    break;
                }
                case 2: {
                    System.out.println("--- CONSULTA Y BÚSQUEDA ---");
                    System.out.println("1. Buscar por Título");
                    System.out.println("2. Buscar por Nacionalidad");
                    System.out.println("3. Buscar por Productora");
                    System.out.println("4. Listar todas");
                    System.out.print("Seleccione: ");
                    int sub = leerOpcion();
                    List<Pelicula> lista = new ArrayList<>();

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
                    } else {
                        System.out.println("Opción no válida.");
                        break;
                    }

                    if (lista.isEmpty()) {
                        System.out.println("No se han encontrado resultados.");
                    } else {
                        lista.forEach(System.out::println);
                    }
                    break;
                }
                case 3: {
                    int idMod = leerIdPositivo("ID de la película a modificar: ");
                    Optional<Pelicula> optPeli = peliculasDAO.buscarPorId(idMod);

                    if (optPeli.isPresent()) {
                        Pelicula p = optPeli.get();
                        System.out.println("Modificando: " + p.getTitulo());

                        System.out.print("Nuevo título (deja en blanco para no cambiar): ");
                        String t = sc.nextLine().trim();
                        if (!t.isBlank()) {
                            if (validarTextoGeneral(t)) p.setTitulo(t);
                            else System.out.println("  ✗ Título no válido, se mantiene el anterior.");
                        }

                        System.out.print("Nueva nacionalidad (deja en blanco para no cambiar): ");
                        String n = sc.nextLine().trim();
                        if (!n.isBlank()) {
                            if (validarNombre(n)) p.setNacionalidad(n);
                            else System.out.println("  ✗ Nacionalidad no válida, se mantiene la anterior.");
                        }

                        System.out.print("Nueva productora (deja en blanco para no cambiar): ");
                        String pr = sc.nextLine().trim();
                        if (!pr.isBlank()) {
                            if (validarTextoGeneral(pr)) p.setProductora(pr);
                            else System.out.println("  ✗ Productora no válida, se mantiene la anterior.");
                        }

                        System.out.print("Nuevo ID Director (0 para omitir): ");
                        String idDStr = sc.nextLine().trim();
                        if (!idDStr.isBlank()) {
                            try {
                                int idD = Integer.parseInt(idDStr);
                                if (idD > 0) p.setIdDirector(idD);
                                else if (idD != 0) System.out.println("  ✗ ID de director no válido, se mantiene el anterior.");
                            } catch (NumberFormatException e) {
                                System.out.println("  ✗ ID no numérico, se mantiene el anterior.");
                            }
                        }

                        peliculasDAO.actualizar(p);
                        System.out.println("Película actualizada con éxito.");
                    } else {
                        System.out.println("Error: No existe película con ID " + idMod);
                    }
                    break;
                }
                case 4: {
                    int idEli = leerIdPositivo("ID de la película a eliminar: ");
                    System.out.print("¿Seguro que desea eliminar? (Si/No): ");
                    String conf = sc.nextLine().trim();
                    if (conf.equalsIgnoreCase("si") || conf.equalsIgnoreCase("sí")) {
                        if (peliculasDAO.eliminarPorId(idEli)) {
                            System.out.println("Película eliminada.");
                        } else {
                            System.out.println("No se pudo eliminar (ID no encontrado).");
                        }
                    } else {
                        System.out.println("Operación cancelada.");
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

    // =========================================================
    //  MENÚ ACTORES
    // =========================================================

    /**
     * Muestra y gestiona el submenú de gestión de actores.
     * Permite al usuario realizar las siguientes operaciones:
     *   Registro de un nuevo actor
     *   Consulta y filtrado de actores por nombre o nacionalidad
     *   Asociación de un actor a una película con un rol determinado
     *   Modificación de los datos de un actor existente
     *   Eliminación de un actor o desvinculación de una película
     * El bucle se mantiene activo hasta que el usuario elige volver al menú principal
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
                case 1: {
                    System.out.println("\n-> Ejecutando: Alta de nuevo actor...");

                    String nombre = leerNombre("Introduce el nombre: ");
                    String nacionalidad = leerNombre("Introduce la nacionalidad: ");

                    String sexo;
                    while (true) {
                        System.out.print("Introduce el sexo (H/M/Otro): ");
                        sexo = sc.nextLine().trim();
                        if (sexo.equalsIgnoreCase("H") || sexo.equalsIgnoreCase("M") || sexo.equalsIgnoreCase("Otro")) break;
                        System.out.println("  ✗ Sexo no válido. Introduce H, M u Otro.");
                    }

                    Actor nuevo = new Actor(0, nombre, nacionalidad, sexo);
                    nuevo = actoresDAO.darDeAlta(nuevo);
                    System.out.println("¡Actor guardado con éxito! ID: " + nuevo.getId());
                    break;
                }
                case 2: {
                    System.out.println("\n-> Ejecutando: Consultar actores...");
                    System.out.println("1. Buscar por nombre");
                    System.out.println("2. Buscar por nacionalidad");
                    System.out.println("3. Listar todos");
                    System.out.print("Elija un filtro: ");
                    int filtro = leerOpcion();

                    List<Actor> resultados = new ArrayList<>();
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
                        break;
                    }

                    if (resultados.isEmpty()) {
                        System.out.println("No se encontraron actores.");
                    } else {
                        resultados.forEach(System.out::println);
                    }
                    break;
                }
                case 3: {
                    System.out.println("\n-> Ejecutando: Asociar actor a película...");
                    int idActorAsoc = leerIdPositivo("Introduce el ID del actor: ");
                    int idPeliculaAsoc = leerIdPositivo("Introduce el ID de la película: ");
                    String rol = leerTexto("Introduce el rol del actor en la película (ej. Protagonista, Extra): ");

                    actoresDAO.asociarAPelicula(idActorAsoc, idPeliculaAsoc, rol);
                    System.out.println("Actor asociado a la película con éxito.");
                    break;
                }
                case 4: {
                    System.out.println("\n-> Ejecutando: Modificar datos...");
                    int idMod = leerIdPositivo("Introduce el ID del actor a modificar: ");

                    Optional<Actor> actorOpt = actoresDAO.buscarPorId(idMod);
                    if (actorOpt.isPresent()) {
                        Actor actorMod = actorOpt.get();
                        System.out.println("Actor actual: " + actorMod);

                        System.out.print("Nuevo nombre (deja en blanco para no cambiar): ");
                        String nNombre = sc.nextLine().trim();
                        if (!nNombre.isBlank()) {
                            if (validarNombre(nNombre)) actorMod.setNombre(nNombre);
                            else System.out.println("  ✗ Nombre no válido, se mantiene el anterior.");
                        }

                        System.out.print("Nueva nacionalidad (deja en blanco para no cambiar): ");
                        String nNac = sc.nextLine().trim();
                        if (!nNac.isBlank()) {
                            if (validarNombre(nNac)) actorMod.setNacionalidad(nNac);
                            else System.out.println("  ✗ Nacionalidad no válida, se mantiene la anterior.");
                        }

                        System.out.print("Nuevo sexo (H/M/Otro, deja en blanco para no cambiar): ");
                        String nSexo = sc.nextLine().trim();
                        if (!nSexo.isBlank()) {
                            if (nSexo.equalsIgnoreCase("H") || nSexo.equalsIgnoreCase("M") || nSexo.equalsIgnoreCase("Otro")) {
                                actorMod.setSexo(nSexo);
                            } else {
                                System.out.println("  ✗ Sexo no válido (H/M/Otro), se mantiene el anterior.");
                            }
                        }

                        actoresDAO.actualizar(actorMod);
                        System.out.println("¡Actor actualizado con éxito!");
                    } else {
                        System.out.println("No existe ningún actor con ese ID.");
                    }
                    break;
                }
                case 5: {
                    System.out.println("\n-> Ejecutando: Eliminar o desvincular actor...");
                    System.out.println("1. Eliminar actor del sistema");
                    System.out.println("2. Desvincular actor de una película");
                    System.out.print("Elija una opción: ");
                    int opcEliminar = leerOpcion();

                    if (opcEliminar == 1) {
                        int idElim = leerIdPositivo("Introduce el ID del actor a eliminar: ");
                        boolean eliminado = actoresDAO.eliminarPorId(idElim);
                        if (eliminado) System.out.println("Actor eliminado correctamente.");
                        else System.out.println("No se pudo eliminar (o no existe).");
                    } else if (opcEliminar == 2) {
                        int idActDesv = leerIdPositivo("Introduce el ID del actor: ");
                        int idPelDesv = leerIdPositivo("Introduce el ID de la película: ");
                        actoresDAO.desvincularDePelicula(idActDesv, idPelDesv);
                        System.out.println("Actor desvinculado de la película.");
                    } else {
                        System.out.println("Opción no válida.");
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

    // =========================================================
    //  MENÚ DIRECTORES
    // =========================================================

    /**
     * Muestra y gestiona el submenú de gestión de directores.
     * Permite al usuario realizar las siguientes operaciones:
     * Registro de un nuevo director
     * Consulta y filtrado de directores por nombre o nacionalidad
     * Asignación de un director a una película
     * Modificación de los datos de un director existente
     * Eliminación de un director por su ID
     * El bucle se mantiene activo hasta que el usuario elige volver al menú principal.
     */
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

                    String nombre = leerNombre("Introduce el nombre: ");
                    String nacionalidad = leerNombre("Introduce la nacionalidad: ");

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
                        break;
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
                    int idDirector = leerIdPositivo("Introduce el ID del director: ");
                    int idPelicula = leerIdPositivo("Introduce el ID de la película: ");

                    directorDAO.asignarAPelicula(idDirector, idPelicula);
                    System.out.println("Director asignado a la película con éxito.");
                    break;
                }
                case 4: {
                    System.out.println("\n-> Ejecutando: Modificar datos...");
                    int idMod = leerIdPositivo("Introduce el ID del director a modificar: ");

                    Optional<Director> directorOpt = directorDAO.buscarPorId(idMod);
                    if (directorOpt.isPresent()) {
                        Director directorMod = directorOpt.get();
                        System.out.println("Director actual: " + directorMod);

                        System.out.print("Nuevo nombre (deja en blanco para no cambiar): ");
                        String nNombre = sc.nextLine().trim();
                        if (!nNombre.isBlank()) {
                            if (validarNombre(nNombre)) directorMod.setNombre(nNombre);
                            else System.out.println("  ✗ Nombre no válido, se mantiene el anterior.");
                        }

                        System.out.print("Nueva nacionalidad (deja en blanco para no cambiar): ");
                        String nNac = sc.nextLine().trim();
                        if (!nNac.isBlank()) {
                            if (validarNombre(nNac)) directorMod.setNacionalidad(nNac);
                            else System.out.println("  ✗ Nacionalidad no válida, se mantiene la anterior.");
                        }

                        directorDAO.actualizar(directorMod);
                        System.out.println("¡Director actualizado con éxito!");
                    } else {
                        System.out.println("No existe ningún director con ese ID.");
                    }
                    break;
                }
                case 5: {
                    System.out.println("\n-> Ejecutando: Eliminar director...");
                    int idElim = leerIdPositivo("Introduce el ID del director a eliminar: ");

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

    // =========================================================
    //  MENÚ EJEMPLARES
    // =========================================================

    /**
     * Muestra y gestiona el submenú de gestión de ejemplares.
     * Permite al usuario realizar las siguientes operaciones:
     * Alta de un nuevo ejemplar asociado a una película
     * Consulta de los ejemplares de una película concreta
     * Actualización del estado de conservación de un ejemplar
     * Eliminación de un ejemplar por su número</li>
     * El bucle se mantiene activo hasta que el usuario elige volver al menú principal.
     *
     * @see modelo.EstadoConservacion
     */
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
                    int idPelicula = leerIdPositivo("Introduce el ID de la película: ");

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
                    int idPelicula = leerIdPositivo("Introduce el ID de la película: ");

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
                    int numEjemplar = leerIdPositivo("Introduce el número del ejemplar: ");

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
                    int numElim = leerIdPositivo("Introduce el número del ejemplar a eliminar: ");

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

    // =========================================================
    //  MENÚ SOCIOS
    // =========================================================

    /**
     * Muestra y gestiona el submenú de gestión de socios.
     * Permite al usuario realizar las siguientes operaciones:
     * Registro de un nuevo socio (con validación de socio aval)
     * Consulta y filtrado de socios por DNI, nombre o teléfono
     * Modificación de los datos de un socio existente
     * Eliminación de un socio por su DNI
     * El bucle se mantiene activo hasta que el usuario elige volver al menú principal.
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

                    String dni = leerDni("Introduce el DNI: ");
                    String nombre = leerNombre("Introduce el nombre: ");
                    String direccion = leerTexto("Introduce la dirección: ");
                    String telefono = leerTelefono("Introduce el teléfono: ");

                    // El socio aval puede ser el mismo DNI (se avala a sí mismo) o un DNI existente
                    String dniAval;
                    while (true) {
                        System.out.print("Introduce el DNI del socio aval (o el mismo DNI si se avala a sí mismo): ");
                        String entrada = sc.nextLine().trim().toUpperCase();
                        if (!validarDni(entrada)) {
                            System.out.println("  ✗ DNI aval no válido. Debe tener 8 dígitos seguidos de la letra correcta.");
                            continue;
                        }
                        // Si se avala a sí mismo no hace falta buscar en la BD
                        if (entrada.equals(dni)) {
                            dniAval = entrada;
                            break;
                        }
                        // Comprobamos que el socio aval existe
                        Optional<Socio> avalOpt = socioDAO.buscarPorDni(entrada);
                        if (avalOpt.isEmpty()) {
                            System.out.println("  ✗ Error: El socio aval con DNI " + entrada + " no existe en el sistema.");
                        } else {
                            dniAval = entrada;
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
                        String dniConsulta = leerDni("Introduce el DNI: ");
                        Optional<Socio> resultado = socioDAO.buscarPorDni(dniConsulta);
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
                    String dniMod = leerDni("Introduce el DNI del socio a modificar: ");

                    Optional<Socio> socioOpt = socioDAO.buscarPorDni(dniMod);
                    if (socioOpt.isPresent()) {
                        Socio socioMod = socioOpt.get();
                        System.out.println("Socio actual: " + socioMod);

                        System.out.print("Nuevo nombre (deja en blanco para no cambiar): ");
                        String nNombre = sc.nextLine().trim();
                        if (!nNombre.isBlank()) {
                            if (validarNombre(nNombre)) socioMod.setNombre(nNombre);
                            else System.out.println("  ✗ Nombre no válido, se mantiene el anterior.");
                        }

                        System.out.print("Nueva dirección (deja en blanco para no cambiar): ");
                        String nDireccion = sc.nextLine().trim();
                        if (!nDireccion.isBlank()) {
                            if (validarTextoGeneral(nDireccion)) socioMod.setDireccion(nDireccion);
                            else System.out.println("  ✗ Dirección no válida, se mantiene la anterior.");
                        }

                        System.out.print("Nuevo teléfono (deja en blanco para no cambiar): ");
                        String nTelefono = sc.nextLine().trim();
                        if (!nTelefono.isBlank()) {
                            if (validarTelefono(nTelefono)) socioMod.setTelefono(nTelefono);
                            else System.out.println("  ✗ Teléfono no válido, se mantiene el anterior.");
                        }

                        String nDniAval = leerDniOpcional("Nuevo DNI aval (deja en blanco para no cambiar): ");
                        if (nDniAval != null) {
                            if (!nDniAval.equals(dniMod) && socioDAO.buscarPorDni(nDniAval).isEmpty()) {
                                System.out.println("  ✗ Error: El socio aval con DNI " + nDniAval + " no existe.");
                            } else {
                                socioMod.setDniAval(nDniAval);
                            }
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
                    String dniElim = leerDni("Introduce el DNI del socio a eliminar: ");

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

    // =========================================================
    //  MENÚ ALQUILERES
    // =========================================================

    /**
     * Muestra y gestiona el submenú de gestión de alquileres.
     * Permite al usuario realizar las siguientes operaciones:
     * Registro de un nuevo alquiler de un ejemplar a un socio
     * Consulta de alquileres activos, histórico, por socio o por película
     * Registro de la devolución de un alquiler
     * Cancelación de un alquiler existente
     * El bucle se mantiene activo hasta que el usuario elige volver al menú principal.
     */
    private static void menuAlquileres() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n--- 3.6 GESTIÓN DE ALQUILERES ---");
            System.out.println("1. Registrar un nuevo alquiler");
            System.out.println("2. Consultar alquileres");
            System.out.println("3. Registrar devolución");
            System.out.println("4. Cancelar alquiler");
            System.out.println("5. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            switch (leerOpcion()) {
                case 1: {
                    System.out.println("\n-> Ejecutando: Registrar nuevo alquiler...");
                    int numEjemplar = leerIdPositivo("Introduce el número del ejemplar: ");
                    String dniSocio = leerDni("Introduce el DNI del socio: ");
                    LocalDate fechaInicio = leerFecha("Introduce la fecha de inicio (AAAA-MM-DD): ");

                    try {
                        Alquiler nuevo = new Alquiler(0, numEjemplar, dniSocio, fechaInicio, null);
                        nuevo = alquilerDAO.registrarAlquiler(nuevo);
                        System.out.println("¡Alquiler registrado con éxito! ID: " + nuevo.getIdAlquiler());
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                }
                case 2: {
                    System.out.println("\n-> Ejecutando: Consultar alquileres...");
                    System.out.println("1. Alquileres activos");
                    System.out.println("2. Histórico de alquileres");
                    System.out.println("3. Buscar por socio (DNI)");
                    System.out.println("4. Buscar por película");
                    System.out.print("Elija un filtro: ");
                    int filtro = leerOpcion();

                    List<Alquiler> resultados = new ArrayList<>();
                    if (filtro == 1) {
                        resultados = alquilerDAO.listarActivos();
                    } else if (filtro == 2) {
                        resultados = alquilerDAO.listarHistorico();
                    } else if (filtro == 3) {
                        String dniConsulta = leerDni("Introduce el DNI del socio: ");
                        resultados = alquilerDAO.buscarPorSocio(dniConsulta);
                    } else if (filtro == 4) {
                        int idPeli = leerIdPositivo("Introduce el ID de la película: ");
                        resultados = alquilerDAO.buscarPorPelicula(idPeli);
                    } else {
                        System.out.println("Filtro no válido.");
                        break;
                    }

                    if (resultados.isEmpty()) {
                        System.out.println("No se encontraron alquileres.");
                    } else {
                        resultados.forEach(System.out::println);
                    }
                    break;
                }
                case 3: {
                    System.out.println("\n-> Ejecutando: Registrar devolución...");
                    int idAlquiler = leerIdPositivo("Introduce el ID del alquiler: ");

                    Optional<Alquiler> alquilerOpt = alquilerDAO.buscarPorId(idAlquiler);
                    if (alquilerOpt.isPresent()) {
                        Alquiler alquiler = alquilerOpt.get();
                        if (!alquiler.isActivo()) {
                            System.out.println("Este alquiler ya fue devuelto el " + alquiler.getFechaDevolucion());
                            break;
                        }
                        System.out.println("Alquiler encontrado: " + alquiler);
                        LocalDate fechaDev = leerFechaOpcional(
                                "Introduce la fecha de devolución (AAAA-MM-DD, o deja en blanco para hoy): ",
                                LocalDate.now()
                        );

                        if (fechaDev.isBefore(alquiler.getFechaInicio())) {
                            System.out.println("  ✗ Error: La fecha de devolución no puede ser anterior a la fecha de inicio ("
                                    + alquiler.getFechaInicio() + ").");
                            break;
                        }

                        try {
                            alquilerDAO.registrarDevolucion(idAlquiler, fechaDev);
                            System.out.println("¡Devolución registrada con éxito!");
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("No existe ningún alquiler con ese ID.");
                    }
                    break;
                }
                case 4: {
                    System.out.println("\n-> Ejecutando: Cancelar alquiler...");
                    int idCancelar = leerIdPositivo("Introduce el ID del alquiler a cancelar: ");

                    Optional<Alquiler> alquilerCancelar = alquilerDAO.buscarPorId(idCancelar);
                    if (alquilerCancelar.isPresent()) {
                        System.out.println("Alquiler encontrado: " + alquilerCancelar.get());
                        try {
                            if (alquilerDAO.cancelarAlquiler(idCancelar)) {
                                System.out.println("Alquiler cancelado correctamente.");
                            } else {
                                System.out.println("No se pudo cancelar el alquiler.");
                            }
                        } catch (AlquilerDAOException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("No existe ningún alquiler con ese ID.");
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
}