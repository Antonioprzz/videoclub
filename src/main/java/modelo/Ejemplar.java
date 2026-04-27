package modelo;

/**
 * Representa un ejemplar físico de una película del videoclub.
 * Cada ejemplar pertenece a una película y tiene un estado de conservación
 * que indica su condición física actual.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 * @see EstadoConservacion
 */
public class Ejemplar {

    /** Número identificador único del ejemplar. */
    private int num_ejemplar;

    /** Identificador de la película a la que pertenece el ejemplar. */
    private int id_pelicula;

    /** Estado de conservación actual del ejemplar. */
    private EstadoConservacion estado;

    /**
     * Constructor por defecto sin argumentos.
     */
    public Ejemplar() {}

    /**
     * Constructor con todos los atributos del ejemplar.
     *
     * @param num_ejemplar número identificador del ejemplar
     * @param id_pelicula  identificador de la película asociada
     * @param estado       estado de conservación del ejemplar
     */
    public Ejemplar(int num_ejemplar, int id_pelicula, EstadoConservacion estado) {
        this.num_ejemplar = num_ejemplar;
        this.id_pelicula = id_pelicula;
        this.estado = estado;
    }

    /**
     * Obtiene el número identificador del ejemplar.
     *
     * @return el número del ejemplar
     */
    public int getNumEjemplar() { return num_ejemplar; }

    /**
     * Establece el número identificador del ejemplar.
     *
     * @param num_ejemplar el nuevo número del ejemplar
     */
    public void setNumEjemplar(int num_ejemplar) { this.num_ejemplar = num_ejemplar; }

    /**
     * Obtiene el identificador de la película asociada.
     *
     * @return el identificador de la película
     */
    public int getIdPelicula() { return id_pelicula; }

    /**
     * Establece el identificador de la película asociada.
     *
     * @param id_pelicula el nuevo identificador de la película
     */
    public void setIdPelicula(int id_pelicula) { this.id_pelicula = id_pelicula; }

    /**
     * Obtiene el estado de conservación del ejemplar.
     *
     * @return el estado de conservación
     */
    public EstadoConservacion getEstado() { return estado; }

    /**
     * Establece el estado de conservación del ejemplar.
     *
     * @param estado el nuevo estado de conservación
     */
    public void setEstado(EstadoConservacion estado) { this.estado = estado; }

    /**
     * Devuelve una representación en cadena del ejemplar.
     *
     * @return cadena con los datos del ejemplar
     */
    @Override
    public String toString() {
        return "Ejemplar{" +
                "num_ejemplar=" + num_ejemplar +
                ", id_pelicula=" + id_pelicula +
                ", estado=" + estado +
                '}';
    }
}
