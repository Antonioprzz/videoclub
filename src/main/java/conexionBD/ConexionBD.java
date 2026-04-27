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
            stmt.execute("CREATE TABLE IF NOT EXISTS Director ("
                    + "Nombre TEXT PRIMARY KEY, "
                    + "Nacionalidad TEXT, "
                    + "Sexo TEXT, "
                    + "Papel TEXT"
                    + ")");

            // Tabla de actores
            stmt.execute("CREATE TABLE IF NOT EXISTS Actor ("
                    + "Nombre TEXT PRIMARY KEY, "
                    + "Nacionalidad TEXT"
                    + ")");

            // Tabla de películas
            stmt.execute("CREATE TABLE IF NOT EXISTS Pelicula ("
                    + "Titulo TEXT PRIMARY KEY, "
                    + "Fecha TEXT, "
                    + "Nacionalidad TEXT, "
                    + "Productora TEXT, "
                    + "NombreDirector TEXT, "
                    + "FOREIGN KEY (NombreDirector) REFERENCES Director(Nombre)"
                    + ")");

            // Tabla Intermedia Pelicula-Actor Reparto (Para el campo Actores[] de cada películo)
            stmt.execute("CREATE TABLE IF NOT EXISTS reparto ("
                    + "nombrePelicula TEXT, "
                    + "nombreActor TEXT, "
                    + "PRIMARY KEY (nombrePelicula, nombreActor), "
                    + "FOREIGN KEY (nombrePelicula) REFERENCES Pelicula(Titulo) ON DELETE CASCADE, "
                    + "FOREIGN KEY (nombreActor) REFERENCES Actor(Nombre) ON DELETE CASCADE"
                    + ")");

            // Tabla de ejemplares
            stmt.execute("CREATE TABLE IF NOT EXISTS Ejemplares ("
                    + "NumEjemplar INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "Estado TEXT, "
                    + "nombrePelicula TEXT, "
                    + "FOREIGN KEY (nombrePelicula) REFERENCES Pelicula(Titulo) ON DELETE CASCADE"
                    + ")");

            // Tabla de socios
            stmt.execute("CREATE TABLE IF NOT EXISTS Socio ("
                    + "DNI TEXT PRIMARY KEY, "
                    + "Nombre TEXT NOT NULL, "
                    + "Direccion TEXT, "
                    + "Telefono TEXT"
                    + ")");

            // Tabla de alquileres
            stmt.execute("CREATE TABLE IF NOT EXISTS Alquiler ("
                    + "CodigoAlquiler INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "Adquisicion TEXT, "
                    + "Limite TEXT, "
                    + "dniSocio TEXT, "
                    + "numEjemplar INTEGER, "
                    + "FOREIGN KEY (dniSocio) REFERENCES Socio(DNI), "
                    + "FOREIGN KEY (numEjemplar) REFERENCES Ejemplares(NumEjemplar)"
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
