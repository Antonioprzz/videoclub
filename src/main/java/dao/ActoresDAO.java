package dao;

import modelo.Actores;

import java.util.List;
import java.util.Optional;

public interface ActoresDAO {
    Actores darDeAlta(Actores actor);
    Optional<Actores> buscarPorId(int id);
    List<Actores> buscarPorNombre(String nombre);
    List<Actores> buscarPorNacionalidad(String nacionalidad);
    List<Actores> listarTodos();
    Actores actualizar(Actores actor);
    boolean eliminarPorId(int id);
}