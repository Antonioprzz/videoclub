package dao;

import modelo.Pelicula;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PeliculaDAOImpl implements PeliculaDAO {

    private final Connection connection;

    public PeliculaDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Pelicula darDeAlta(Pelicula pelicula) {
        String sql = "INSERT INTO peliculas (titulo, nacionalidad, productora, fecha, idDirector) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pelicula.getTitulo());
            stmt.setString(2, pelicula.getNacionalidad());
            stmt.setString(3, pelicula.getProductora());
            stmt.setString(4, pelicula.getFecha().toString());
            stmt.setInt(5, pelicula.getIdDirector());
            stmt.executeUpdate();

            try (ResultSet llave = stmt.getGeneratedKeys()) {
                if (llave.next()) {
                    pelicula.setId(llave.getInt(1));
                }
            }
            return pelicula;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la película: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Pelicula> buscarPorId(int id) {
        String sql = "SELECT titulo, nacionalidad, productora, fecha, idDirector FROM peliculas WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearFila(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar película por id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pelicula> buscarPorTitulo(String titulo) {
        String sql = "SELECT id, titulo, nacionalidad, productora, fecha, idDirector FROM peliculas WHERE titulo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titulo);
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar películas por título: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pelicula> buscarPorNacionalidad(String nacionalidad) {
        String sql = "SELECT id, titulo, nacionalidad, productora, fecha, idDirector FROM peliculas WHERE nacionalidad = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nacionalidad);
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar películas por nacionalidad: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pelicula> buscarPorProductora(String productora) {
        String sql = "SELECT id, titulo, nacionalidad, productora, fecha, idDirector FROM peliculas WHERE productora = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productora);
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar películas por productora: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pelicula> listarTodas() {
        String sql = "SELECT id, titulo, nacionalidad, productora, fecha, idDirector FROM peliculas";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todas las películas: " + e.getMessage(), e);
        }
    }

    @Override
    public Pelicula actualizar(Pelicula pelicula) {
        String sql = "UPDATE peliculas SET titulo = ?, nacionalidad = ?, productora = ?, fecha = ?, idDirector = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pelicula.getTitulo());
            stmt.setString(2, pelicula.getNacionalidad());
            stmt.setString(3, pelicula.getProductora());
            stmt.setString(4, pelicula.getFecha().toString());
            stmt.setInt(5, pelicula.getIdDirector());
            stmt.setInt(6, pelicula.getId());
            stmt.executeUpdate();
            return pelicula;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la película: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminarPorId(int id) {
        String sql = "DELETE FROM peliculas WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la película: " + e.getMessage(), e);
        }
    }

    private Pelicula mapearFila(ResultSet rs) throws SQLException {
        return new Pelicula(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("nacionalidad"),
                rs.getString("productora"),
                LocalDate.parse(rs.getString("fecha")),
                rs.getInt("idDirector")
        );
    }

    private List<Pelicula> mapearFilas(ResultSet rs) throws SQLException {
        List<Pelicula> peliculas = new ArrayList<>();
        try (rs) {
            while (rs.next()) {
                peliculas.add(mapearFila(rs));
            }
        }
        return peliculas;
    }
}