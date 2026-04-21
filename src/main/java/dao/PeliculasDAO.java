package dao;

import modelo.Peliculas;

import java.util.List;
import java.util.Optional;

public interface PeliculasDAO {
    Peliculas darDeAlta(Peliculas pelicula);
    Optional<Peliculas> buscarPorId(int id);
    List<Peliculas> buscarPorTitulo(String titulo);
    List<Peliculas> buscarPorNacionalidad(String nacionalidad);
    List<Peliculas> buscarPorProductora(String productora);
    List<Peliculas> listarTodas();
    Peliculas actualizar(Peliculas pelicula);
    boolean eliminarPorId(int id);
}
