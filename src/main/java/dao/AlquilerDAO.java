package dao;

import modelo.Alquiler;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO (Data Access Object) para la gestión de alquileres.
 * Define las operaciones de registro, consulta, devolución y cancelación
 * de alquileres de ejemplares de películas en el videoclub.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 * @see Alquiler
 * @see AlquilerDAOImpl
 */
public interface AlquilerDAO {

    /**
     * Registra un nuevo alquiler en la base de datos.
     * Verifica previamente que el ejemplar no esté ya alquilado.
     *
     * @param alquiler el alquiler a registrar
     * @return el alquiler registrado con su identificador generado
     * @throws AlquilerDAOException si el ejemplar ya está alquilado o hay un error de acceso a datos
     */
    Alquiler registrarAlquiler(Alquiler alquiler);

    /**
     * Busca un alquiler por su identificador único.
     *
     * @param id_alquiler el identificador del alquiler
     * @return un {@link Optional} con el alquiler encontrado, o vacío si no existe
     */
    Optional<Alquiler> buscarPorId(int id_alquiler);

    /**
     * Lista todos los alquileres activos (sin fecha de devolución).
     *
     * @return lista de alquileres activos
     */
    List<Alquiler> listarActivos();

    /**
     * Lista el histórico de alquileres ya devueltos.
     *
     * @return lista de alquileres con fecha de devolución registrada
     */
    List<Alquiler> listarHistorico();

    /**
     * Busca todos los alquileres realizados por un socio.
     *
     * @param dni_socio el DNI del socio
     * @return lista de alquileres del socio
     */
    List<Alquiler> buscarPorSocio(String dni_socio);

    /**
     * Busca todos los alquileres asociados a una película.
     *
     * @param id_pelicula el identificador de la película
     * @return lista de alquileres de la película
     */
    List<Alquiler> buscarPorPelicula(int id_pelicula);

    /**
     * Registra la devolución de un alquiler activo.
     *
     * @param id_alquiler     el identificador del alquiler
     * @param fechaDevolucion la fecha de devolución
     * @return el alquiler actualizado con la fecha de devolución
     * @throws AlquilerDAOException si el alquiler no existe o ya fue devuelto
     */
    Alquiler registrarDevolucion(int id_alquiler, LocalDate fechaDevolucion);

    /**
     * Cancela (elimina) un alquiler de la base de datos.
     *
     * @param id_alquiler el identificador del alquiler a cancelar
     * @return {@code true} si se canceló correctamente, {@code false} en caso contrario
     */
    boolean cancelarAlquiler(int id_alquiler);
}
