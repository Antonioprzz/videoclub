package dao;

import modelo.Ejemplar;
import modelo.EstadoConservacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de la interfaz {@link EjemplarDAO}.
 * Proporciona el acceso a datos de ejemplares mediante consultas SQL
 * sobre una conexión JDBC a la base de datos del videoclub.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 * @see EjemplarDAO
 * @see EjemplarDAOException
 */
public class EjemplarDAOImpl implements EjemplarDAO {

    /** Conexión JDBC a la base de datos. */
    private final Connection connection;

    /**
     * Crea una nueva instancia de {@code EjemplarDAOImpl} con la conexión proporcionada.
     *
     * @param connection la conexión JDBC a la base de datos
     */
    public EjemplarDAOImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     *
     * @throws EjemplarDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public Ejemplar darDeAlta(Ejemplar ejemplar) {
        String sql = "INSERT INTO ejemplar (id_pelicula, estado) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, ejemplar.getIdPelicula());
            stmt.setString(2, ejemplar.getEstado().name());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    ejemplar.setNumEjemplar(keys.getInt(1));
                }
            }
            return ejemplar;
        } catch (SQLException e) {
            throw new EjemplarDAOException("Error al dar de alta el ejemplar: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws EjemplarDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public Optional<Ejemplar> buscarPorId(int num_ejemplar) {
        String sql = "SELECT num_ejemplar, id_pelicula, estado FROM ejemplar WHERE num_ejemplar = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, num_ejemplar);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearFila(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new EjemplarDAOException("Error al buscar ejemplar por id: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws EjemplarDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public List<Ejemplar> buscarPorPelicula(int id_pelicula) {
        String sql = "SELECT num_ejemplar, id_pelicula, estado FROM ejemplar WHERE id_pelicula = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_pelicula);
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new EjemplarDAOException("Error al buscar ejemplares por película: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws EjemplarDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public Ejemplar actualizarEstado(int num_ejemplar, EstadoConservacion nuevoEstado) {
        String sql = "UPDATE ejemplar SET estado = ? WHERE num_ejemplar = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado.name());
            stmt.setInt(2, num_ejemplar);
            stmt.executeUpdate();
            return buscarPorId(num_ejemplar).orElseThrow(() ->
                    new EjemplarDAOException("Ejemplar no encontrado tras actualizar.", null));
        } catch (SQLException e) {
            throw new EjemplarDAOException("Error al actualizar el estado: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws EjemplarDAOException si el ejemplar está alquilado o hay un error de acceso a datos
     */
    @Override
    public boolean eliminarPorId(int num_ejemplar) {
        // Comprobamos primero que no esté alquilado
        String sqlCheck = "SELECT COUNT(*) FROM alquiler WHERE num_ejemplar = ? AND fecha_devolucion IS NULL";
        String sqlDelete = "DELETE FROM ejemplar WHERE num_ejemplar = ?";
        try {
            try (PreparedStatement stmtCheck = connection.prepareStatement(sqlCheck)) {
                stmtCheck.setInt(1, num_ejemplar);
                try (ResultSet rs = stmtCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new EjemplarDAOException("No se puede eliminar: el ejemplar está actualmente alquilado.", null);
                    }
                }
            }
            try (PreparedStatement stmtDelete = connection.prepareStatement(sqlDelete)) {
                stmtDelete.setInt(1, num_ejemplar);
                return stmtDelete.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new EjemplarDAOException("Error al eliminar el ejemplar: " + e.getMessage(), e);
        }
    }

    /**
     * Mapea una fila del {@link ResultSet} a un objeto {@link Ejemplar}.
     *
     * @param rs el ResultSet posicionado en la fila a mapear
     * @return el ejemplar mapeado
     * @throws SQLException si ocurre un error al leer los datos del ResultSet
     */
    private Ejemplar mapearFila(ResultSet rs) throws SQLException {
        return new Ejemplar(
                rs.getInt("num_ejemplar"),
                rs.getInt("id_pelicula"),
                EstadoConservacion.valueOf(rs.getString("estado"))
        );
    }

    /**
     * Mapea todas las filas de un {@link ResultSet} a una lista de objetos {@link Ejemplar}.
     *
     * @param rs el ResultSet a recorrer
     * @return lista de ejemplares mapeados
     * @throws SQLException si ocurre un error al leer los datos del ResultSet
     */
    private List<Ejemplar> mapearFilas(ResultSet rs) throws SQLException {
        List<Ejemplar> ejemplares = new ArrayList<>();
        try (rs) {
            while (rs.next()) {
                ejemplares.add(mapearFila(rs));
            }
        }
        return ejemplares;
    }
}
