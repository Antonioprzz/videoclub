package modelo;

import java.time.LocalDate;

/**
 * Representa un alquiler de un ejemplar de película por parte de un socio.
 * Contiene la información del alquiler: identificador, ejemplar alquilado,
 * socio que realiza el alquiler, fecha de inicio y fecha de devolución.
 * Un alquiler se considera activo si la fecha de devolución es {@code null}.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 */
public class Alquiler {

    /** Identificador único del alquiler. */
    private int id_alquiler;

    /** Número del ejemplar alquilado. */
    private int num_ejemplar;

    /** DNI del socio que realiza el alquiler. */
    private String dni_socio;

    /** Fecha de inicio del alquiler. */
    private LocalDate fecha_inicio;

    /** Fecha de devolución del ejemplar. {@code null} si el alquiler sigue activo. */
    private LocalDate fecha_devolucion;

    /**
     * Constructor por defecto sin argumentos.
     */
    public Alquiler() {}

    /**
     * Constructor con todos los atributos del alquiler.
     *
     * @param id_alquiler     identificador único del alquiler
     * @param num_ejemplar    número del ejemplar alquilado
     * @param dni_socio       DNI del socio que alquila
     * @param fecha_inicio    fecha de inicio del alquiler
     * @param fecha_devolucion fecha de devolución, o {@code null} si sigue activo
     */
    public Alquiler(int id_alquiler, int num_ejemplar, String dni_socio, LocalDate fecha_inicio, LocalDate fecha_devolucion) {
        this.id_alquiler = id_alquiler;
        this.num_ejemplar = num_ejemplar;
        this.dni_socio = dni_socio;
        this.fecha_inicio = fecha_inicio;
        this.fecha_devolucion = fecha_devolucion;
    }

    /**
     * Obtiene el identificador del alquiler.
     *
     * @return el identificador del alquiler
     */
    public int getIdAlquiler() { return id_alquiler; }

    /**
     * Establece el identificador del alquiler.
     *
     * @param id_alquiler el nuevo identificador del alquiler
     */
    public void setIdAlquiler(int id_alquiler) { this.id_alquiler = id_alquiler; }

    /**
     * Obtiene el número del ejemplar alquilado.
     *
     * @return el número del ejemplar
     */
    public int getNumEjemplar() { return num_ejemplar; }

    /**
     * Establece el número del ejemplar alquilado.
     *
     * @param num_ejemplar el nuevo número de ejemplar
     */
    public void setNumEjemplar(int num_ejemplar) { this.num_ejemplar = num_ejemplar; }

    /**
     * Obtiene el DNI del socio que realiza el alquiler.
     *
     * @return el DNI del socio
     */
    public String getDniSocio() { return dni_socio; }

    /**
     * Establece el DNI del socio que realiza el alquiler.
     *
     * @param dni_socio el nuevo DNI del socio
     */
    public void setDniSocio(String dni_socio) { this.dni_socio = dni_socio; }

    /**
     * Obtiene la fecha de inicio del alquiler.
     *
     * @return la fecha de inicio
     */
    public LocalDate getFechaInicio() { return fecha_inicio; }

    /**
     * Establece la fecha de inicio del alquiler.
     *
     * @param fecha_inicio la nueva fecha de inicio
     */
    public void setFechaInicio(LocalDate fecha_inicio) { this.fecha_inicio = fecha_inicio; }

    /**
     * Obtiene la fecha de devolución del ejemplar.
     *
     * @return la fecha de devolución, o {@code null} si el alquiler sigue activo
     */
    public LocalDate getFechaDevolucion() { return fecha_devolucion; }

    /**
     * Establece la fecha de devolución del ejemplar.
     *
     * @param fecha_devolucion la nueva fecha de devolución
     */
    public void setFechaDevolucion(LocalDate fecha_devolucion) { this.fecha_devolucion = fecha_devolucion; }

    /**
     * Indica si el alquiler está activo (sin devolver).
     *
     * @return {@code true} si la fecha de devolución es {@code null}, {@code false} en caso contrario
     */
    public boolean isActivo() { return fecha_devolucion == null; }

    /**
     * Devuelve una representación en cadena del alquiler.
     *
     * @return cadena con los datos del alquiler
     */
    @Override
    public String toString() {
        return "Alquiler{" +
                "id_alquiler=" + id_alquiler +
                ", num_ejemplar=" + num_ejemplar +
                ", dni_socio='" + dni_socio + '\'' +
                ", fecha_inicio=" + fecha_inicio +
                ", fecha_devolucion=" + (fecha_devolucion != null ? fecha_devolucion : "Activo") +
                '}';
    }
}
