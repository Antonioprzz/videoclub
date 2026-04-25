package dao;

import modelo.Pelicula;

import java.util.List;
import java.util.Optional;

public interface PeliculaDAO {
    Pelicula darDeAlta(Pelicula pelicula);
    Optional<Pelicula> buscarPorId(int id);
    List<Pelicula> buscarPorTitulo(String titulo);
    List<Pelicula> buscarPorNacionalidad(String nacionalidad);
    List<Pelicula> buscarPorProductora(String productora);
    List<Pelicula> listarTodas();
    Pelicula actualizar(Pelicula pelicula);
    boolean eliminarPorId(int id);
}
