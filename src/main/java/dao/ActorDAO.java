package dao;

import modelo.Actor;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO (Data Access Object) para la gestión de actores.
 * Define las operaciones CRUD y de asociación de actores con películas
 * en la base de datos del videoclub.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 * @see Actor
 * @see ActorDAOImpl
 */
public interface ActorDAO {

    /**
     * Da de alta un nuevo actor en la base de datos.
     *
     * @param actor el actor a registrar
     * @return el actor registrado con su identificador generado
     */
    Actor darDeAlta(Actor actor);

    /**
     * Busca un actor por su identificador único.
     *
     * @param id_actor el identificador del actor
     * @return un {@link Optional} con el actor encontrado, o vacío si no existe
     */
    Optional<Actor> buscarPorId(int id_actor);

    /**
     * Busca actores por su nombre.
     *
     * @param nombre el nombre a buscar
     * @return lista de actores que coinciden con el nombre
     */
    List<Actor> buscarPorNombre(String nombre);

    /**
     * Busca actores por su nacionalidad.
     *
     * @param nacionalidad la nacionalidad a buscar
     * @return lista de actores que coinciden con la nacionalidad
     */
    List<Actor> buscarPorNacionalidad(String nacionalidad);

    /**
     * Lista todos los actores registrados en la base de datos.
     *
     * @return lista con todos los actores
     */
    List<Actor> listarTodos();

    /**
     * Actualiza los datos de un actor existente.
     *
     * @param actor el actor con los datos actualizados
     * @return el actor actualizado
     */
    Actor actualizar(Actor actor);

    /**
     * Elimina un actor de la base de datos por su identificador.
     *
     * @param id_actor el identificador del actor a eliminar
     * @return {@code true} si se eliminó correctamente, {@code false} en caso contrario
     */
    boolean eliminarPorId(int id_actor);

    /**
     * Asocia un actor a una película con un rol determinado.
     *
     * @param idActor    el identificador del actor
     * @param idPelicula el identificador de la película
     * @param rol        el rol del actor en la película (ej. Protagonista, Extra)
     */
    void asociarAPelicula(int idActor, int idPelicula, String rol);

    /**
     * Desvincula un actor de una película.
     *
     * @param idActor    el identificador del actor
     * @param idPelicula el identificador de la película
     */
    void desvincularDePelicula(int idActor, int idPelicula);
}