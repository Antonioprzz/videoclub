package modelo;

import java.time.LocalDate;

/**
 * Representa una película del catálogo del videoclub.
 * Contiene la información básica de una película: identificador, título,
 * nacionalidad, productora, fecha de estreno y el director asociado.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 */
public class Pelicula {

    /** Identificador único de la película en la base de datos. */
    private int id;

    /** Título de la película. */
    private String titulo;

    /** Nacionalidad o país de origen de la película. */
    private String nacionalidad;

    /** Nombre de la productora de la película. */
    private String productora;

    /** Fecha de estreno de la película. */
    private LocalDate fecha;

    /** Identificador del director de la película. */
    private int idDirector;

    /**
     * Constructor por defecto sin argumentos.
     */
    public Pelicula() {
    }

    /**
     * Constructor con todos los atributos de la película.
     *
     * @param id           identificador único de la película
     * @param titulo       título de la película
     * @param nacionalidad nacionalidad de la película
     * @param productora   nombre de la productora
     * @param fecha        fecha de estreno
     * @param idDirector   identificador del director
     */
    public Pelicula(int id, String titulo, String nacionalidad, String productora,
                    LocalDate fecha, int idDirector) {
        this.id = id;
        this.titulo = titulo;
        this.nacionalidad = nacionalidad;
        this.productora = productora;
        this.fecha = fecha;
        this.idDirector = idDirector;
    }

    /**
     * Obtiene el identificador de la película.
     *
     * @return el identificador de la película
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador de la película.
     *
     * @param id el nuevo identificador de la película
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el título de la película.
     *
     * @return el título de la película
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título de la película.
     *
     * @param titulo el nuevo título de la película
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene la nacionalidad de la película.
     *
     * @return la nacionalidad de la película
     */
    public String getNacionalidad() {
        return nacionalidad;
    }

    /**
     * Establece la nacionalidad de la película.
     *
     * @param nacionalidad la nueva nacionalidad de la película
     */
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    /**
     * Obtiene el nombre de la productora.
     *
     * @return el nombre de la productora
     */
    public String getProductora() {
        return productora;
    }

    /**
     * Establece el nombre de la productora.
     *
     * @param productora el nuevo nombre de la productora
     */
    public void setProductora(String productora) {
        this.productora = productora;
    }

    /**
     * Obtiene la fecha de estreno de la película.
     *
     * @return la fecha de estreno
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha de estreno de la película.
     *
     * @param fecha la nueva fecha de estreno
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el identificador del director de la película.
     *
     * @return el identificador del director
     */
    public int getIdDirector() {
        return idDirector;
    }

    /**
     * Establece el identificador del director de la película.
     *
     * @param idDirector el nuevo identificador del director
     */
    public void setIdDirector(int idDirector) {
        this.idDirector = idDirector;
    }

    /**
     * Devuelve una representación en cadena de la película.
     *
     * @return cadena con los datos de la película
     */
    @Override
    public String toString() {
        return "Pelicula{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", productora='" + productora + '\'' +
                ", fecha=" + fecha +
                ", idDirector=" + idDirector +
                '}';
    }
}
