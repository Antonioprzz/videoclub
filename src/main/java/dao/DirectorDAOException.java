package dao;

public class DirectorDAOException extends RuntimeException {
    public DirectorDAOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}