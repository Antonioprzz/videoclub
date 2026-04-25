package dao;

public class ActoresDAOException extends RuntimeException {
    public ActoresDAOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
