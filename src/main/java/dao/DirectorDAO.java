package dao;

import modelo.Director;
import java.util.List;
import java.util.Optional;

public interface DirectorDAO {
    Director darDeAlta(Director director);
    Optional<Director> buscarPorId(int id_director);
    List<Director> buscarPorNombre(String nombre);
    List<Director> buscarPorNacionalidad(String nacionalidad);
    List<Director> listarTodos();
    Director actualizar(Director director);
    boolean eliminarPorId(int id_director);
    void asignarAPelicula(int idDirector, int idPelicula);
}
