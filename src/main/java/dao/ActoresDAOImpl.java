package dao;

import modelo.Actores;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActoresDAOImpl implements ActoresDAO {

    private final Connection connection;

    public ActoresDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Actores darDeAlta(Actores actor) {
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

    @Override
    public Optional<Actores> buscarPorId(int id) {
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
            throw new ActoresDAOException("Error al guardar el actor: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Actores> buscarPorNombre(String nombre) {
        String sql = "SELECT id_actor, nombre, nacionalidad, sexo FROM actor WHERE nombre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al guardar el actor: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Actores> buscarPorNacionalidad(String nacionalidad) {
        String sql = "SELECT id_actor, nombre, nacionalidad, sexo FROM actor WHERE nacionalidad = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nacionalidad);
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al guardar el actor: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Actores> listarTodos() {
        String sql = "SELECT id_actor, nombre, nacionalidad, sexo FROM actor";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al guardar el actor: " + e.getMessage(), e);
        }
    }

    @Override
    public Actores actualizar(Actores actor) {
        String sql = "UPDATE actor SET nombre = ?, nacionalidad = ?, sexo = ? WHERE id_actor = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, actor.getNombre());
            stmt.setString(2, actor.getNacionalidad());
            stmt.setString(3, actor.getSexo());
            stmt.setInt(4, actor.getId());
            stmt.executeUpdate();
            return actor;
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al guardar el actor: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminarPorId(int id) {
        String sql = "DELETE FROM actor WHERE id_actor = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al guardar el actor: " + e.getMessage(), e);
        }
    }

    private Actores mapearFila(ResultSet rs) throws SQLException {
        return new Actores(
                rs.getInt("id_actor"),
                rs.getString("nombre"),
                rs.getString("nacionalidad"),
                rs.getString("sexo")
        );
    }

    private List<Actores> mapearFilas(ResultSet rs) throws SQLException {
        List<Actores> actores = new ArrayList<>();
        try (rs) {
            while (rs.next()) {
                actores.add(mapearFila(rs));
            }
        }
        return actores;
    }

    @Override
    public void asociarAPelicula(int id_actor, int idPelicula, String rol) {
        String sql = "INSERT INTO actor_pelicula (id_actor, id_pelicula, rol) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_actor);
            stmt.setInt(2, idPelicula);
            stmt.setString(3, rol);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al guardar el actor: " + e.getMessage(), e);
        }
    }

    @Override
    public void desvincularDePelicula(int idActor, int idPelicula) {
        String sql = "DELETE FROM actor_pelicula WHERE id_actor = ? AND id_pelicula = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idActor);
            stmt.setInt(2, idPelicula);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ActoresDAOException("Error al guardar el actor: " + e.getMessage(), e);
        }
    }
}