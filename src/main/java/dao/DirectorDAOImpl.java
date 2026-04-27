package dao;

import modelo.Director;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de la interfaz {@link DirectorDAO}.
 * Proporciona el acceso a datos de directores mediante consultas SQL
 * sobre una conexión JDBC a la base de datos del videoclub.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 * @see DirectorDAO
 * @see DirectorDAOException
 */
public class DirectorDAOImpl implements DirectorDAO {

    /**
     * Conexión JDBC a la base de datos.
     */
    private final Connection connection;

    /**
     * Crea una nueva instancia de {@code DirectorDAOImpl} con la conexión proporcionada.
     *
     * @param connection la conexión JDBC a la base de datos
     */
    public DirectorDAOImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     *
     * @throws DirectorDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public Director darDeAlta(Director director) {
        String sql = "INSERT INTO director (nombre, nacionalidad) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, director.getNombre());
            stmt.setString(2, director.getNacionalidad());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    director.setId(keys.getInt(1));
                }
            }
            return director;
        } catch (SQLException e) {
            throw new DirectorDAOException("Error al registrar el director: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws DirectorDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public Optional<Director> buscarPorId(int id_director) {
        String sql = "SELECT id_director, nombre, nacionalidad FROM director WHERE id_director = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_director);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearFila(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DirectorDAOException("Error al buscar director por id: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws DirectorDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public List<Director> buscarPorNombre(String nombre) {
        String sql = "SELECT id_director, nombre, nacionalidad FROM director WHERE nombre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new DirectorDAOException("Error al buscar director por nombre: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws DirectorDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public List<Director> buscarPorNacionalidad(String nacionalidad) {
        String sql = "SELECT id_director, nombre, nacionalidad FROM director WHERE nacionalidad = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nacionalidad);
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new DirectorDAOException("Error al buscar director por nacionalidad: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws DirectorDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public List<Director> listarTodos() {
        String sql = "SELECT id_director, nombre, nacionalidad FROM director";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new DirectorDAOException("Error al listar directores: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws DirectorDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public Director actualizar(Director director) {
        String sql = "UPDATE director SET nombre = ?, nacionalidad = ? WHERE id_director = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, director.getNombre());
            stmt.setString(2, director.getNacionalidad());
            stmt.setInt(3, director.getId());
            stmt.executeUpdate();
            return director;
        } catch (SQLException e) {
            throw new DirectorDAOException("Error al actualizar el director: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws DirectorDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public boolean eliminarPorId(int id_director) {
        String sql = "DELETE FROM director WHERE id_director = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_director);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DirectorDAOException("Error al eliminar el director: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws DirectorDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public void asignarAPelicula(int idDirector, int idPelicula) {
        String sql = "UPDATE pelicula SET id_director = ? WHERE id_pelicula = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idDirector);
            stmt.setInt(2, idPelicula);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DirectorDAOException("Error al asignar director a película: " + e.getMessage(), e);
        }
    }

    /**
     * Mapea una fila del {@link ResultSet} a un objeto {@link Director}.
     *
     * @param rs el ResultSet posicionado en la fila a mapear
     * @return el director mapeado
     * @throws SQLException si ocurre un error al leer los datos del ResultSet
     */
    private Director mapearFila(ResultSet rs) throws SQLException {
        return new Director(
                rs.getInt("id_director"),
                rs.getString("nombre"),
                rs.getString("nacionalidad")
        );
    }

    /**
     * Mapea todas las filas de un {@link ResultSet} a una lista de objetos {@link Director}.
     *
     * @param rs el ResultSet a recorrer
     * @return lista de directores mapeados
     * @throws SQLException si ocurre un error al leer los datos del ResultSet
     */
    private List<Director> mapearFilas(ResultSet rs) throws SQLException {
        List<Director> directores = new ArrayList<>();
        try (rs) {
            while (rs.next()) {
                directores.add(mapearFila(rs));
            }
        }
        return directores;
    }
}

