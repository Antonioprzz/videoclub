package dao;

/**
 * Excepción personalizada para errores en las operaciones DAO de actores.
 * Se lanza cuando ocurre un error durante el acceso a datos relacionados
 * con la entidad {@link modelo.Actor}.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 */
public class ActoresDAOException extends RuntimeException {

    /**
     * Crea una nueva excepción con un mensaje descriptivo y la causa original.
     *
     * @param mensaje descripción del error ocurrido
     * @param causa   la excepción original que provocó el error, o {@code null}
     */
    public ActoresDAOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
