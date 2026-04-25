package dao;

public class AlquilerDAOException extends RuntimeException {
    public AlquilerDAOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
