package dao;

import modelo.Socio;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO (Data Access Object) para la gestión de socios.
 * Define las operaciones CRUD para los socios del videoclub.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 * @see Socio
 * @see SocioDAOImpl
 */
public interface SocioDAO {

    /**
     * Da de alta un nuevo socio en la base de datos.
     *
     * @param socio el socio a registrar
     * @return el socio registrado
     */
    Socio darDeAlta(Socio socio);

    /**
     * Busca un socio por su DNI.
     *
     * @param dni el DNI del socio
     * @return un {@link Optional} con el socio encontrado, o vacío si no existe
     */
    Optional<Socio> buscarPorDni(String dni);

    /**
     * Busca socios por su nombre.
     *
     * @param nombre el nombre a buscar
     * @return lista de socios que coinciden con el nombre
     */
    List<Socio> buscarPorNombre(String nombre);

    /**
     * Busca socios por su número de teléfono.
     *
     * @param telefono el teléfono a buscar
     * @return lista de socios que coinciden con el teléfono
     */
    List<Socio> buscarPorTelefono(String telefono);

    /**
     * Lista todos los socios registrados en la base de datos.
     *
     * @return lista con todos los socios
     */
    List<Socio> listarTodos();

    /**
     * Actualiza los datos de un socio existente.
     *
     * @param socio el socio con los datos actualizados
     * @return el socio actualizado
     */
    Socio actualizar(Socio socio);

    /**
     * Elimina un socio de la base de datos por su DNI.
     * No se permite eliminar un socio que tenga alquileres activos
     * o que esté avalando a otro socio.
     *
     * @param dni el DNI del socio a eliminar
     * @return {@code true} si se eliminó correctamente, {@code false} en caso contrario
     * @throws SocioDAOException si el socio tiene alquileres activos o avala a otro socio
     */
    boolean eliminarPorDni(String dni);
}
