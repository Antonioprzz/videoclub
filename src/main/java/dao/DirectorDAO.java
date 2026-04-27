package dao;

import modelo.Director;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO (Data Access Object) para la gestión de directores.
 * Define las operaciones CRUD y de asignación de directores a películas
 * en la base de datos del videoclub.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 * @see Director
 * @see DirectorDAOImpl
 */
public interface DirectorDAO {

    /**
     * Da de alta un nuevo director en la base de datos.
     *
     * @param director el director a registrar
     * @return el director registrado con su identificador generado
     */
    Director darDeAlta(Director director);

    /**
     * Busca un director por su identificador único.
     *
     * @param id_director el identificador del director
     * @return un {@link Optional} con el director encontrado, o vacío si no existe
     */
    Optional<Director> buscarPorId(int id_director);

    /**
     * Busca directores por su nombre.
     *
     * @param nombre el nombre a buscar
     * @return lista de directores que coinciden con el nombre
     */
    List<Director> buscarPorNombre(String nombre);

    /**
     * Busca directores por su nacionalidad.
     *
     * @param nacionalidad la nacionalidad a buscar
     * @return lista de directores que coinciden con la nacionalidad
     */
    List<Director> buscarPorNacionalidad(String nacionalidad);

    /**
     * Lista todos los directores registrados en la base de datos.
     *
     * @return lista con todos los directores
     */
    List<Director> listarTodos();

    /**
     * Actualiza los datos de un director existente.
     *
     * @param director el director con los datos actualizados
     * @return el director actualizado
     */
    Director actualizar(Director director);

    /**
     * Elimina un director de la base de datos por su identificador.
     *
     * @param id_director el identificador del director a eliminar
     * @return {@code true} si se eliminó correctamente, {@code false} en caso contrario
     */
    boolean eliminarPorId(int id_director);

    /**
     * Asigna un director a una película existente.
     *
     * @param idDirector el identificador del director
     * @param idPelicula el identificador de la película
     */
    void asignarAPelicula(int idDirector, int idPelicula);
}
