package modelo;

import java.time.LocalDate;

public class Pelicula {
    private int id;
    private String titulo;
    private String nacionalidad;
    private String productora;
    private LocalDate fecha;
    private int idDirector;

    public Pelicula() {
    }

    public Pelicula(int id, String titulo, String nacionalidad, String productora,
                    LocalDate fecha, int idDirector) {
        this.id = id;
        this.titulo = titulo;
        this.nacionalidad = nacionalidad;
        this.productora = productora;
        this.fecha = fecha;
        this.idDirector = idDirector;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getProductora() {
        return productora;
    }

    public void setProductora(String productora) {
        this.productora = productora;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getIdDirector() {
        return idDirector;
    }

    public void setIdDirector(int idDirector) {
        this.idDirector = idDirector;
    }

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
