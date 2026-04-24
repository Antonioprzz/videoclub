package modelo;

public class Ejemplar {

    private int num_ejemplar;
    private int id_pelicula;
    private EstadoConservacion estado;

    public Ejemplar() {}

    public Ejemplar(int num_ejemplar, int id_pelicula, EstadoConservacion estado) {
        this.num_ejemplar = num_ejemplar;
        this.id_pelicula = id_pelicula;
        this.estado = estado;
    }

    public int getNumEjemplar() { return num_ejemplar; }
    public void setNumEjemplar(int num_ejemplar) { this.num_ejemplar = num_ejemplar; }

    public int getIdPelicula() { return id_pelicula; }
    public void setIdPelicula(int id_pelicula) { this.id_pelicula = id_pelicula; }

    public EstadoConservacion getEstado() { return estado; }
    public void setEstado(EstadoConservacion estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Ejemplar{" +
                "num_ejemplar=" + num_ejemplar +
                ", id_pelicula=" + id_pelicula +
                ", estado=" + estado +
                '}';
    }
}
