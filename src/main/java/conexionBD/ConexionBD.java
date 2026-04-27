package conexionBD;

/**
 * Clase encargada de gestionar la conexión con la base de datos del videoclub.
 * Proporciona la configuración y los métodos necesarios para establecer
 * y administrar la conexión JDBC con la base de datos SQLite.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestor de la conexión a la base de datos SQLite del videoclub.
 * Proporciona conexión y creación del esquema.
 */
public class ConexionBD {

    private static final String DB_URL = "jdbc:sqlite:videoclub.db";
    private Connection connection;

    /**
     * Obtiene la conexión a la base de datos, creándola si no existe.
     */
    public Connection obtenerConexion() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL);
            } catch (SQLException e) {
                throw new RuntimeException("Error al conectar con la base de datos: " + e.getMessage(), e);
            }
        }
        return connection;
    }

    /**
     * Inicializa el esquema de la base de datos creando las tablas e índices necesarios.
     */
    public void inicializarBaseDatos() {
        Connection conn = obtenerConexion();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL");
            stmt.execute("PRAGMA foreign_keys=ON");

            // Tabla de directores
            stmt.execute("CREATE TABLE IF NOT EXISTS director ("
                    + "id_director INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nombre TEXT NOT NULL, "
                    + "nacionalidad TEXT"
                    + ")");

// Tabla de actores
            stmt.execute("CREATE TABLE IF NOT EXISTS actor ("
                    + "id_actor INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nombre TEXT NOT NULL, "
                    + "nacionalidad TEXT, "
                    + "sexo TEXT"
                    + ")");

// Tabla de películas
            stmt.execute("CREATE TABLE IF NOT EXISTS peliculas ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "titulo TEXT NOT NULL, "
                    + "nacionalidad TEXT, "
                    + "productora TEXT, "
                    + "fecha TEXT, "
                    + "idDirector INTEGER, "
                    + "FOREIGN KEY (idDirector) REFERENCES director(id_director)"
                    + ")");

// Tabla intermedia Actor-Película
            stmt.execute("CREATE TABLE IF NOT EXISTS actor_pelicula ("
                    + "id_actor INTEGER, "
                    + "id_pelicula INTEGER, "
                    + "rol TEXT, "
                    + "PRIMARY KEY (id_actor, id_pelicula), "
                    + "FOREIGN KEY (id_actor) REFERENCES actor(id_actor) ON DELETE CASCADE, "
                    + "FOREIGN KEY (id_pelicula) REFERENCES peliculas(id) ON DELETE CASCADE"
                    + ")");

// Tabla de ejemplares
            stmt.execute("CREATE TABLE IF NOT EXISTS ejemplar ("
                    + "num_ejemplar INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "id_pelicula INTEGER, "
                    + "estado TEXT, "
                    + "FOREIGN KEY (id_pelicula) REFERENCES peliculas(id) ON DELETE CASCADE"
                    + ")");

// Tabla de socios
            stmt.execute("CREATE TABLE IF NOT EXISTS socio ("
                    + "dni TEXT PRIMARY KEY, "
                    + "nombre TEXT NOT NULL, "
                    + "direccion TEXT, "
                    + "telefono TEXT, "
                    + "dni_aval TEXT, "
                    + "FOREIGN KEY (dni_aval) REFERENCES socio(dni)"
                    + ")");

// Tabla de alquileres
            stmt.execute("CREATE TABLE IF NOT EXISTS alquiler ("
                    + "id_alquiler INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "num_ejemplar INTEGER, "
                    + "dni_socio TEXT, "
                    + "fecha_inicio TEXT, "
                    + "fecha_devolucion TEXT, "
                    + "FOREIGN KEY (num_ejemplar) REFERENCES ejemplar(num_ejemplar), "
                    + "FOREIGN KEY (dni_socio) REFERENCES socio(dni)"
                    + ")");

        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar la base de datos: " + e.getMessage(), e);
        }
    }

    /**
     * Cierra la conexión a la base de datos.
     */
    public void cerrar() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error al cerrar la conexión a la base de datos: " + e.getMessage(), e);
            }
        }
    }
}
