package dao;

import modelo.Socio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SocioDAOImpl implements SocioDAO {

    private final Connection connection;

    public SocioDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Socio darDeAlta(Socio socio) {
        String sql = "INSERT INTO socio (dni, nombre, direccion, telefono, dni_aval) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, socio.getDni());
            stmt.setString(2, socio.getNombre());
            stmt.setString(3, socio.getDireccion());
            stmt.setString(4, socio.getTelefono());
            stmt.setString(5, socio.getDniAval());
            stmt.executeUpdate();
            return socio;
        } catch (SQLException e) {
            throw new SocioDAOException("Error al registrar el socio: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Socio> buscarPorDni(String dni) {
        String sql = "SELECT dni, nombre, direccion, telefono, dni_aval FROM socio WHERE dni = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearFila(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new SocioDAOException("Error al buscar socio por DNI: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Socio> buscarPorNombre(String nombre) {
        String sql = "SELECT dni, nombre, direccion, telefono, dni_aval FROM socio WHERE nombre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new SocioDAOException("Error al buscar socio por nombre: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Socio> buscarPorTelefono(String telefono) {
        String sql = "SELECT dni, nombre, direccion, telefono, dni_aval FROM socio WHERE telefono = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, telefono);
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new SocioDAOException("Error al buscar socio por teléfono: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Socio> listarTodos() {
        String sql = "SELECT dni, nombre, direccion, telefono, dni_aval FROM socio";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            return mapearFilas(stmt.executeQuery());
        } catch (SQLException e) {
            throw new SocioDAOException("Error al listar socios: " + e.getMessage(), e);
        }
    }

    @Override
    public Socio actualizar(Socio socio) {
        String sql = "UPDATE socio SET nombre = ?, direccion = ?, telefono = ?, dni_aval = ? WHERE dni = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, socio.getNombre());
            stmt.setString(2, socio.getDireccion());
            stmt.setString(3, socio.getTelefono());
            stmt.setString(4, socio.getDniAval());
            stmt.setString(5, socio.getDni());
            stmt.executeUpdate();
            return socio;
        } catch (SQLException e) {
            throw new SocioDAOException("Error al actualizar el socio: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminarPorDni(String dni) {
        // Comprobamos que no tenga alquileres activos
        String sqlCheckAlquiler = "SELECT COUNT(*) FROM alquiler WHERE dni_socio = ? AND fecha_devolucion IS NULL";
        // Comprobamos que no esté avalando a otro socio activo
        String sqlCheckAval = "SELECT COUNT(*) FROM socio WHERE dni_aval = ? AND dni != ?";
        String sqlDelete = "DELETE FROM socio WHERE dni = ?";

        try {
            try (PreparedStatement stmtAlquiler = connection.prepareStatement(sqlCheckAlquiler)) {
                stmtAlquiler.setString(1, dni);
                try (ResultSet rs = stmtAlquiler.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SocioDAOException("No se puede eliminar: el socio tiene alquileres activos.", null);
                    }
                }
            }
            try (PreparedStatement stmtAval = connection.prepareStatement(sqlCheckAval)) {
                stmtAval.setString(1, dni);
                stmtAval.setString(2, dni);
                try (ResultSet rs = stmtAval.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SocioDAOException("No se puede eliminar: el socio está avalando a otro socio activo.", null);
                    }
                }
            }
            try (PreparedStatement stmtDelete = connection.prepareStatement(sqlDelete)) {
                stmtDelete.setString(1, dni);
                return stmtDelete.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new SocioDAOException("Error al eliminar el socio: " + e.getMessage(), e);
        }
    }

    private Socio mapearFila(ResultSet rs) throws SQLException {
        return new Socio(
                rs.getString("dni"),
                rs.getString("nombre"),
                rs.getString("direccion"),
                rs.getString("telefono"),
                rs.getString("dni_aval")
        );
    }

    private List<Socio> mapearFilas(ResultSet rs) throws SQLException {
        List<Socio> socios = new ArrayList<>();
        try (rs) {
            while (rs.next()) {
                socios.add(mapearFila(rs));
            }
        }
        return socios;
    }
}
