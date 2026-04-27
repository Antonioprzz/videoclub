package dao;

import modelo.Actor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de la interfaz {@link ActorDAO}.
 * Proporciona el acceso a datos de actores mediante consultas SQL
 * sobre una conexión JDBC a la base de datos del videoclub.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 * @see ActorDAO
 * @see ActoresDAOException
 */
public class ActorDAOImpl implements ActorDAO {

    /** Conexión JDBC a la base de datos. */
    private final Connection connection;

    /**
     * Crea una nueva instancia de {@code ActorDAOImpl} con la conexión proporcionada.
     *
     * @param connection la conexión JDBC a la base de datos
     */
    public ActorDAOImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     *
     * @throws ActoresDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public Actor darDeAlta(Actor actor) {
        String sql = "INSERT INTO actor (nombre, nacionalidad, sexo) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, actor.getNombre());
            stmt.setString(2, actor.getNacionalidad());
            stmt.setString(3, actor.getSexo());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    actor.setId(keys.getInt(1));
                }
            }
            return actor;
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al guardar el actor: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws ActoresDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public Optional<Actor> buscarPorId(int id) {
        String sql = "SELECT id_actor, nombre, nacionalidad, sexo FROM actor WHERE id_actor = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearFila(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al buscar el actor por id: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws ActoresDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public List<Actor> buscarPorNombre(String nombre) {
        String sql = "SELECT id_actor, nombre, nacionalidad, sexo FROM actor WHERE nombre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al buscar el actor por nombre: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws ActoresDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public List<Actor> buscarPorNacionalidad(String nacionalidad) {
        String sql = "SELECT id_actor, nombre, nacionalidad, sexo FROM actor WHERE nacionalidad = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nacionalidad);
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al buscar el actor por nacionalidad: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws ActoresDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public List<Actor> listarTodos() {
        String sql = "SELECT id_actor, nombre, nacionalidad, sexo FROM actor";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al listar los actores: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws ActoresDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public Actor actualizar(Actor actor) {
        String sql = "UPDATE actor SET nombre = ?, nacionalidad = ?, sexo = ? WHERE id_actor = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, actor.getNombre());
            stmt.setString(2, actor.getNacionalidad());
            stmt.setString(3, actor.getSexo());
            stmt.setInt(4, actor.getId());
            stmt.executeUpdate();
            return actor;
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al actualizar el actor: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws ActoresDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public boolean eliminarPorId(int id) {
        String sql = "DELETE FROM actor WHERE id_actor = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al eliminar el actor por id: " + e.getMessage(), e);
        }
    }

    /**
     * Mapea una fila del {@link ResultSet} a un objeto {@link Actor}.
     *
     * @param rs el ResultSet posicionado en la fila a mapear
     * @return el actor mapeado
     * @throws SQLException si ocurre un error al leer los datos del ResultSet
     */
    private Actor mapearFila(ResultSet rs) throws SQLException {
        return new Actor(
                rs.getInt("id_actor"),
                rs.getString("nombre"),
                rs.getString("nacionalidad"),
                rs.getString("sexo")
        );
    }

    /**
     * Mapea todas las filas de un {@link ResultSet} a una lista de objetos {@link Actor}.
     *
     * @param rs el ResultSet a recorrer
     * @return lista de actores mapeados
     * @throws SQLException si ocurre un error al leer los datos del ResultSet
     */
    private List<Actor> mapearFilas(ResultSet rs) throws SQLException {
        List<Actor> actores = new ArrayList<>();
        try (rs) {
            while (rs.next()) {
                actores.add(mapearFila(rs));
            }
        }
        return actores;
    }

    /**
     *
     * @throws ActoresDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public void asociarAPelicula(int id_actor, int idPelicula, String rol) {
        String sql = "INSERT INTO actor_pelicula (id_actor, id_pelicula, rol) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_actor);
            stmt.setInt(2, idPelicula);
            stmt.setString(3, rol);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al asociar un actor a una película: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @throws ActoresDAOException si ocurre un error de acceso a la base de datos
     */
    @Override
    public void desvincularDePelicula(int idActor, int idPelicula) {
        String sql = "DELETE FROM actor_pelicula WHERE id_actor = ? AND id_pelicula = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idActor);
            stmt.setInt(2, idPelicula);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al desvincular un actor de una película: " + e.getMessage(), e);
        }
    }
}