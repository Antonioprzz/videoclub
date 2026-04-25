package dao;

import modelo.Actor;

import java.util.List;
import java.util.Optional;

public interface ActorDAO {
    Actor darDeAlta(Actor actor);
    Optional<Actor> buscarPorId(int id_actor);
    List<Actor> buscarPorNombre(String nombre);
    List<Actor> buscarPorNacionalidad(String nacionalidad);
    List<Actor> listarTodos();
    Actor actualizar(Actor actor);
    boolean eliminarPorId(int id_actor);
    void asociarAPelicula(int idActor, int idPelicula, String rol);
    void desvincularDePelicula(int idActor, int idPelicula);
}