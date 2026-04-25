package dao;

public class SocioDAOException extends RuntimeException {
    public SocioDAOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
