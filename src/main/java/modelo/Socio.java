package modelo;

public class Socio {

    private String dni;
    private String nombre;
    private String direccion;
    private String telefono;
    private String dni_aval;

    public Socio() {}

    public Socio(String dni, String nombre, String direccion, String telefono, String dni_aval) {
        this.dni = dni;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.dni_aval = dni_aval;
    }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDniAval() { return dni_aval; }
    public void setDniAval(String dni_aval) { this.dni_aval = dni_aval; }

    @Override
    public String toString() {
        return "Socio{" +
                "dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", dni_aval='" + dni_aval + '\'' +
                '}';
    }
}
