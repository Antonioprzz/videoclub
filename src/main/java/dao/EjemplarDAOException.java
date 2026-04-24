package dao;

public class EjemplarDAOException extends RuntimeException {
    public EjemplarDAOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
