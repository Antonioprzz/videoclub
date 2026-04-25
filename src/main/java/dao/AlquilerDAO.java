package dao;

import modelo.Alquiler;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AlquilerDAO {
    Alquiler registrarAlquiler(Alquiler alquiler);
    Optional<Alquiler> buscarPorId(int id_alquiler);
    List<Alquiler> listarActivos();
    List<Alquiler> listarHistorico();
    List<Alquiler> buscarPorSocio(String dni_socio);
    List<Alquiler> buscarPorPelicula(int id_pelicula);
    Alquiler registrarDevolucion(int id_alquiler, LocalDate fechaDevolucion);
    boolean cancelarAlquiler(int id_alquiler);
}
