package dao;

import modelo.Alquiler;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de la interfaz {@link AlquilerDAO}.
 * Proporciona el acceso a datos de alquileres mediante consultas SQL
 * sobre una conexión JDBC a la base de datos del videoclub.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 * @see AlquilerDAO
 * @see AlquilerDAOException
 */
public class AlquilerDAOImpl implements AlquilerDAO {

    /** Conexión JDBC a la base de datos. */
    private final Connection connection;

    /**
     * Crea una nueva instancia de {@code AlquilerDAOImpl} con la conexión proporcionada.
     *
     * @param connection la conexión JDBC a la base de datos
     */
    public AlquilerDAOImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     *
     * @throws AlquilerDAOException si el ejemplar ya está alquilado o hay un error de acceso a datos
     */
    @Override
    public Alquiler registrarAlquiler(Alquiler alquiler) {
        // Comprobamos que el ejemplar no esté ya alquilado
        String sqlCheck = "SELECT COUNT(*) FROM alquiler WHERE num_ejemplar = ? AND fecha_devolucion IS NULL";
        String sql = "INSERT INTO alquiler (num_ejemplar, dni_socio, fecha_inicio, fecha_devolucion) VALUES (?, ?, ?, ?)";
        try {
            try (PreparedStatement stmtCheck = connection.prepareStatement(sqlCheck)) {
                stmtCheck.setInt(1, alquiler.getNumEjemplar());
                try (ResultSet rs = stmtCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new AlquilerDAOException("No se puede alquilar: el ejemplar ya está alquilado.", null);
                    }
                }
            }
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, alquiler.getNumEjemplar());
                stmt.setString(2, alquiler.getDniSocio());
                stmt.setDate(3, Date.valueOf(alquiler.getFechaInicio()));
                stmt.setNull(4, Types.DATE);
                stmt.executeUpdate();
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        alquiler.setIdAlquiler(keys.getInt(1));
                    }
                }
            }
            return alquiler;
        } catch (SQLException e) {
            throw new AlquilerDAOException("Error al registrar el alquiler: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws AlquilerDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public Optional<Alquiler> buscarPorId(int id_alquiler) {
        String sql = "SELECT id_alquiler, num_ejemplar, dni_socio, fecha_inicio, fecha_devolucion FROM alquiler WHERE id_alquiler = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_alquiler);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearFila(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new AlquilerDAOException("Error al buscar alquiler por id: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws AlquilerDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public List<Alquiler> listarActivos() {
        String sql = "SELECT id_alquiler, num_ejemplar, dni_socio, fecha_inicio, fecha_devolucion FROM alquiler WHERE fecha_devolucion IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new AlquilerDAOException("Error al listar alquileres activos: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws AlquilerDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public List<Alquiler> listarHistorico() {
        String sql = "SELECT id_alquiler, num_ejemplar, dni_socio, fecha_inicio, fecha_devolucion FROM alquiler WHERE fecha_devolucion IS NOT NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new AlquilerDAOException("Error al listar histórico de alquileres: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws AlquilerDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public List<Alquiler> buscarPorSocio(String dni_socio) {
        String sql = "SELECT id_alquiler, num_ejemplar, dni_socio, fecha_inicio, fecha_devolucion FROM alquiler WHERE dni_socio = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni_socio);
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new AlquilerDAOException("Error al buscar alquileres por socio: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws AlquilerDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public List<Alquiler> buscarPorPelicula(int id_pelicula) {
        String sql = "SELECT a.id_alquiler, a.num_ejemplar, a.dni_socio, a.fecha_inicio, a.fecha_devolucion " +
                "FROM alquiler a JOIN ejemplar e ON a.num_ejemplar = e.num_ejemplar " +
                "WHERE e.id_pelicula = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_pelicula);
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new AlquilerDAOException("Error al buscar alquileres por película: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws AlquilerDAOException si el alquiler no existe, ya fue devuelto, o hay un error de acceso a datos
     */
    @Override
    public Alquiler registrarDevolucion(int id_alquiler, LocalDate fechaDevolucion) {
        String sql = "UPDATE alquiler SET fecha_devolucion = ? WHERE id_alquiler = ? AND fecha_devolucion IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(fechaDevolucion));
            stmt.setInt(2, id_alquiler);
            int filas = stmt.executeUpdate();
            if (filas == 0) {
                throw new AlquilerDAOException("El alquiler no existe o ya fue devuelto.", null);
            }
            return buscarPorId(id_alquiler).orElseThrow(() ->
                    new AlquilerDAOException("Alquiler no encontrado tras registrar devolución.", null));
        } catch (SQLException e) {
            throw new AlquilerDAOException("Error al registrar la devolución: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws AlquilerDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public boolean cancelarAlquiler(int id_alquiler) {
        String sql = "DELETE FROM alquiler WHERE id_alquiler = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_alquiler);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AlquilerDAOException("Error al cancelar el alquiler: " + e.getMessage(), e);
        }
    }

    /**
     * Mapea una fila del {@link ResultSet} a un objeto {@link Alquiler}.
     *
     * @param rs el ResultSet posicionado en la fila a mapear
     * @return el alquiler mapeado
     * @throws SQLException si ocurre un error al leer los datos del ResultSet
     */
    private Alquiler mapearFila(ResultSet rs) throws SQLException {
        Date fechaDev = rs.getDate("fecha_devolucion");
        return new Alquiler(
                rs.getInt("id_alquiler"),
                rs.getInt("num_ejemplar"),
                rs.getString("dni_socio"),
                rs.getDate("fecha_inicio").toLocalDate(),
                fechaDev != null ? fechaDev.toLocalDate() : null
        );
    }

    /**
     * Mapea todas las filas de un {@link ResultSet} a una lista de objetos {@link Alquiler}.
     *
     * @param rs el ResultSet a recorrer
     * @return lista de alquileres mapeados
     * @throws SQLException si ocurre un error al leer los datos del ResultSet
     */
    private List<Alquiler> mapearFilas(ResultSet rs) throws SQLException {
        List<Alquiler> alquileres = new ArrayList<>();
        try (rs) {
            while (rs.next()) {
                alquileres.add(mapearFila(rs));
            }
        }
        return alquileres;
    }
}