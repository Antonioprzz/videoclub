package modelo;

/**
 * Enumeración que representa los posibles estados de conservación
 * de un ejemplar físico de película en el videoclub.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 */
public enum EstadoConservacion {

    /** El ejemplar está en estado nuevo, sin uso previo. */
    NUEVO,

    /** El ejemplar se encuentra en buen estado de conservación. */
    BUENO,

    /** El ejemplar presenta signos de deterioro. */
    DETERIORADO,

    /** El ejemplar se encuentra en mal estado. */
    MAL_ESTADO
}
