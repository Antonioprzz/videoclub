package modelo;

import java.time.LocalDate;

public class Alquiler {

    private int id_alquiler;
    private int num_ejemplar;
    private String dni_socio;
    private LocalDate fecha_inicio;
    private LocalDate fecha_devolucion;

    public Alquiler() {}

    public Alquiler(int id_alquiler, int num_ejemplar, String dni_socio, LocalDate fecha_inicio, LocalDate fecha_devolucion) {
        this.id_alquiler = id_alquiler;
        this.num_ejemplar = num_ejemplar;
        this.dni_socio = dni_socio;
        this.fecha_inicio = fecha_inicio;
        this.fecha_devolucion = fecha_devolucion;
    }

    public int getIdAlquiler() { return id_alquiler; }
    public void setIdAlquiler(int id_alquiler) { this.id_alquiler = id_alquiler; }

    public int getNumEjemplar() { return num_ejemplar; }
    public void setNumEjemplar(int num_ejemplar) { this.num_ejemplar = num_ejemplar; }

    public String getDniSocio() { return dni_socio; }
    public void setDniSocio(String dni_socio) { this.dni_socio = dni_socio; }

    public LocalDate getFechaInicio() { return fecha_inicio; }
    public void setFechaInicio(LocalDate fecha_inicio) { this.fecha_inicio = fecha_inicio; }

    public LocalDate getFechaDevolucion() { return fecha_devolucion; }
    public void setFechaDevolucion(LocalDate fecha_devolucion) { this.fecha_devolucion = fecha_devolucion; }

    public boolean isActivo() { return fecha_devolucion == null; }

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
