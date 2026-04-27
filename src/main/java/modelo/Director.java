package modelo;

/**
 * Representa un director de películas del videoclub.
 * Contiene la información básica de un director: identificador, nombre
 * y nacionalidad.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 */
public class Director {

    /** Identificador único del director en la base de datos. */
    private int id_director;

    /** Nombre completo del director. */
    private String nombre;

    /** Nacionalidad del director. */
    private String nacionalidad;

    /**
     * Constructor por defecto sin argumentos.
     */
    public Director() {}

    /**
     * Constructor con todos los atributos del director.
     *
     * @param id_director  identificador único del director
     * @param nombre       nombre completo del director
     * @param nacionalidad nacionalidad del director
     */
    public Director(int id_director, String nombre, String nacionalidad) {
        this.id_director = id_director;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
    }

    /**
     * Obtiene el identificador del director.
     *
     * @return el identificador del director
     */
    public int getId() { return id_director; }

    /**
     * Establece el identificador del director.
     *
     * @param id_director el nuevo identificador del director
     */
    public void setId(int id_director) { this.id_director = id_director; }

    /**
     * Obtiene el nombre del director.
     *
     * @return el nombre del director
     */
    public String getNombre() { return nombre; }

    /**
     * Establece el nombre del director.
     *
     * @param nombre el nuevo nombre del director
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene la nacionalidad del director.
     *
     * @return la nacionalidad del director
     */
    public String getNacionalidad() { return nacionalidad; }

    /**
     * Establece la nacionalidad del director.
     *
     * @param nacionalidad la nueva nacionalidad del director
     */
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    /**
     * Devuelve una representación en cadena del director.
     *
     * @return cadena con los datos del director
     */
    @Override
    public String toString() {
        return "Director{" +
                "id=" + id_director +
                ", nombre='" + nombre + '\'' +
                ", nacionalidad='" + nacionalidad + '\'' +
                '}';
    }
}
