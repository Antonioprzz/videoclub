package modelo;

/**
 * Representa un actor del videoclub.
 * Contiene la información básica de un actor: identificador, nombre,
 * nacionalidad y sexo.
 *
 * @author Antonio Pérez, Antonio Béltran, Daniel Del Toro, Sergio Ojeda y Juan María Alanis
 * @version 1.0
 */
public class Actor {

    /** Identificador único del actor en la base de datos. */
    private int id_actor;

    /** Nombre completo del actor. */
    private String nombre;

    /** Nacionalidad del actor. */
    private String nacionalidad;

    /** Sexo del actor. */
    private String sexo;

    /**
     * Constructor por defecto sin argumentos.
     */
    public Actor() {
    }

    /**
     * Constructor con todos los atributos del actor.
     *
     * @param id_actor     identificador único del actor
     * @param nombre       nombre completo del actor
     * @param nacionalidad nacionalidad del actor
     * @param sexo         sexo del actor
     */
    public Actor(int id_actor, String nombre, String nacionalidad, String sexo) {
        this.id_actor = id_actor;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.sexo = sexo;
    }

    /**
     * Obtiene el identificador del actor.
     *
     * @return el identificador del actor
     */
    public int getId() {
        return id_actor;
    }

    /**
     * Establece el identificador del actor.
     *
     * @param id el nuevo identificador del actor
     */
    public void setId(int id) {
        this.id_actor = id;
    }

    /**
     * Obtiene el nombre del actor.
     *
     * @return el nombre del actor
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del actor.
     *
     * @param nombre el nuevo nombre del actor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la nacionalidad del actor.
     *
     * @return la nacionalidad del actor
     */
    public String getNacionalidad() {
        return nacionalidad;
    }

    /**
     * Establece la nacionalidad del actor.
     *
     * @param nacionalidad la nueva nacionalidad del actor
     */
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    /**
     * Obtiene el sexo del actor.
     *
     * @return el sexo del actor
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * Establece el sexo del actor.
     *
     * @param sexo el nuevo sexo del actor
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * Devuelve una representación en cadena del actor.
     *
     * @return cadena con los datos del actor
     */
    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id_actor +
                ", nombre='" + nombre + '\'' +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", sexo='" + sexo + '\'' +
                '}';
    }
}