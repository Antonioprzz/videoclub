package dao;

import modelo.Pelicula;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO (Data Access Object) para la gestión de películas.
 * Define las operaciones CRUD para las películas del catálogo
 * del videoclub.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 * @see Pelicula
 * @see PeliculaDAOImpl
 */
public interface PeliculaDAO {

    /**
     * Da de alta una nueva película en la base de datos.
     *
     * @param pelicula la película a registrar
     * @return la película registrada con su identificador generado
     */
    Pelicula darDeAlta(Pelicula pelicula);

    /**
     * Busca una película por su identificador único.
     *
     * @param id el identificador de la película
     * @return un {@link Optional} con la película encontrada, o vacío si no existe
     */
    Optional<Pelicula> buscarPorId(int id);

    /**
     * Busca películas por su título.
     *
     * @param titulo el título a buscar
     * @return lista de películas que coinciden con el título
     */
    List<Pelicula> buscarPorTitulo(String titulo);

    /**
     * Busca películas por su nacionalidad.
     *
     * @param nacionalidad la nacionalidad a buscar
     * @return lista de películas que coinciden con la nacionalidad
     */
    List<Pelicula> buscarPorNacionalidad(String nacionalidad);

    /**
     * Busca películas por su productora.
     *
     * @param productora el nombre de la productora a buscar
     * @return lista de películas que coinciden con la productora
     */
    List<Pelicula> buscarPorProductora(String productora);

    /**
     * Lista todas las películas registradas en la base de datos.
     *
     * @return lista con todas las películas
     */
    List<Pelicula> listarTodas();

    /**
     * Actualiza los datos de una película existente.
     *
     * @param pelicula la película con los datos actualizados
     * @return la película actualizada
     */
    Pelicula actualizar(Pelicula pelicula);

    /**
     * Elimina una película de la base de datos por su identificador.
     *
     * @param id el identificador de la película a eliminar
     * @return {@code true} si se eliminó correctamente, {@code false} en caso contrario
     */
    boolean eliminarPorId(int id);
}
