package modelo;

public class Actores {

    private int id_actor;
    private String nombre;
    private String nacionalidad;
    private String sexo;

    public Actores() {
    }

    public Actores(int id_actor, String nombre, String nacionalidad, String sexo) {
        this.id_actor = id_actor;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.sexo = sexo;
    }

    public int getId() {
        return id_actor;
    }

    public void setId(int id) {
        this.id_actor = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @Override
    public String toString() {
        return "Actores{" +
                "id=" + id_actor +
                ", nombre='" + nombre + '\'' +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", sexo='" + sexo + '\'' +
                '}';
    }
}