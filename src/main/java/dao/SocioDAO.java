package dao;

import modelo.Socio;
import java.util.List;
import java.util.Optional;

public interface SocioDAO {
    Socio darDeAlta(Socio socio);
    Optional<Socio> buscarPorDni(String dni);
    List<Socio> buscarPorNombre(String nombre);
    List<Socio> buscarPorTelefono(String telefono);
    List<Socio> listarTodos();
    Socio actualizar(Socio socio);
    boolean eliminarPorDni(String dni);
}
