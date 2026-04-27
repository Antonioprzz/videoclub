package dao;

import modelo.Ejemplar;
import modelo.EstadoConservacion;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO (Data Access Object) para la gestión de ejemplares.
 * Define las operaciones de alta, búsqueda, actualización de estado
 * y eliminación de ejemplares físicos de películas en el videoclub.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 * @see Ejemplar
 * @see EjemplarDAOImpl
 */
public interface EjemplarDAO {

    /**
     * Da de alta un nuevo ejemplar en la base de datos.
     *
     * @param ejemplar el ejemplar a registrar
     * @return el ejemplar registrado con su número generado
     */
    Ejemplar darDeAlta(Ejemplar ejemplar);

    /**
     * Busca un ejemplar por su número identificador.
     *
     * @param num_ejemplar el número del ejemplar
     * @return un {@link Optional} con el ejemplar encontrado, o vacío si no existe
     */
    Optional<Ejemplar> buscarPorId(int num_ejemplar);

    /**
     * Busca todos los ejemplares asociados a una película.
     *
     * @param id_pelicula el identificador de la película
     * @return lista de ejemplares de la película
     */
    List<Ejemplar> buscarPorPelicula(int id_pelicula);

    /**
     * Actualiza el estado de conservación de un ejemplar.
     *
     * @param num_ejemplar el número del ejemplar a actualizar
     * @param nuevoEstado  el nuevo estado de conservación
     * @return el ejemplar actualizado
     * @throws EjemplarDAOException si el ejemplar no se encuentra tras la actualización
     */
    Ejemplar actualizarEstado(int num_ejemplar, EstadoConservacion nuevoEstado);

    /**
     * Elimina un ejemplar de la base de datos por su número.
     * No se permite eliminar un ejemplar que esté actualmente alquilado.
     *
     * @param num_ejemplar el número del ejemplar a eliminar
     * @return {@code true} si se eliminó correctamente, {@code false} en caso contrario
     * @throws EjemplarDAOException si el ejemplar está actualmente alquilado
     */
    boolean eliminarPorId(int num_ejemplar);
}