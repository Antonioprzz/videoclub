package dao;

import modelo.Director;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DirectorDAOImpl implements DirectorDAO {

    private final Connection connection;

    public DirectorDAOImpl(Connection connection) {
        this.connection = connection;
    }

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

    @Override
    public List<Director> listarTodos() {
        String sql = "SELECT id_director, nombre, nacionalidad FROM director";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new DirectorDAOException("Error al listar directores: " + e.getMessage(), e);
        }
    }

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

    private Director mapearFila(ResultSet rs) throws SQLException {
        return new Director(
                rs.getInt("id_director"),
                rs.getString("nombre"),
                rs.getString("nacionalidad")
        );
    }

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
