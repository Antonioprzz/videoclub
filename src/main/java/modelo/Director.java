package modelo;

public class Director {

    private int id_director;
    private String nombre;
    private String nacionalidad;

    public Director() {}

    public Director(int id_director, String nombre, String nacionalidad) {
        this.id_director = id_director;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
    }

    public int getId() { return id_director; }
    public void setId(int id_director) { this.id_director = id_director; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    @Override
    public String toString() {
        return "Director{" +
                "id=" + id_director +
                ", nombre='" + nombre + '\'' +
                ", nacionalidad='" + nacionalidad + '\'' +
                '}';
    }
}
