package modelo;

/**
 * Representa un socio del videoclub.
 * Contiene la información personal del socio: DNI, nombre, dirección,
 * teléfono y el DNI del socio que actúa como aval.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 */
public class Socio {

    /** DNI del socio (clave primaria). */
    private String dni;

    /** Nombre completo del socio. */
    private String nombre;

    /** Dirección postal del socio. */
    private String direccion;

    /** Número de teléfono del socio. */
    private String telefono;

    /** DNI del socio que actúa como aval. */
    private String dni_aval;

    /**
     * Constructor por defecto sin argumentos.
     */
    public Socio() {}

    /**
     * Constructor con todos los atributos del socio.
     *
     * @param dni       DNI del socio
     * @param nombre    nombre completo del socio
     * @param direccion dirección postal del socio
     * @param telefono  número de teléfono del socio
     * @param dni_aval  DNI del socio aval
     */
    public Socio(String dni, String nombre, String direccion, String telefono, String dni_aval) {
        this.dni = dni;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.dni_aval = dni_aval;
    }

    /**
     * Obtiene el DNI del socio.
     *
     * @return el DNI del socio
     */
    public String getDni() { return dni; }

    /**
     * Establece el DNI del socio.
     *
     * @param dni el nuevo DNI del socio
     */
    public void setDni(String dni) { this.dni = dni; }

    /**
     * Obtiene el nombre del socio.
     *
     * @return el nombre del socio
     */
    public String getNombre() { return nombre; }

    /**
     * Establece el nombre del socio.
     *
     * @param nombre el nuevo nombre del socio
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene la dirección del socio.
     *
     * @return la dirección del socio
     */
    public String getDireccion() { return direccion; }

    /**
     * Establece la dirección del socio.
     *
     * @param direccion la nueva dirección del socio
     */
    public void setDireccion(String direccion) { this.direccion = direccion; }

    /**
     * Obtiene el teléfono del socio.
     *
     * @return el teléfono del socio
     */
    public String getTelefono() { return telefono; }

    /**
     * Establece el teléfono del socio.
     *
     * @param telefono el nuevo teléfono del socio
     */
    public void setTelefono(String telefono) { this.telefono = telefono; }

    /**
     * Obtiene el DNI del socio aval.
     *
     * @return el DNI del socio aval
     */
    public String getDniAval() { return dni_aval; }

    /**
     * Establece el DNI del socio aval.
     *
     * @param dni_aval el nuevo DNI del socio aval
     */
    public void setDniAval(String dni_aval) { this.dni_aval = dni_aval; }

    /**
     * Devuelve una representación en cadena del socio.
     *
     * @return cadena con los datos del socio
     */
    @Override
    public String toString() {
        return "Socio{" +
                "dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", dni_aval='" + dni_aval + '\'' +
                '}';
    }
}
