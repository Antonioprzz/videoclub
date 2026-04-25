package dao;

import modelo.Ejemplar;
import modelo.EstadoConservacion;
import java.util.List;
import java.util.Optional;

public interface EjemplarDAO {
    Ejemplar darDeAlta(Ejemplar ejemplar);
    Optional<Ejemplar> buscarPorId(int num_ejemplar);
    List<Ejemplar> buscarPorPelicula(int id_pelicula);
    Ejemplar actualizarEstado(int num_ejemplar, EstadoConservacion nuevoEstado);
    boolean eliminarPorId(int num_ejemplar);
}