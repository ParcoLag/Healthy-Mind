package healthymind.modelo;

public class Usuario {

    private int idUsuario;
    private String nombre;
    private int edad;
    private String correo;
    private String contrasena;
    private String nacionalidad;
    private String telefono;
    private String tipoUsuario;

    public Usuario() {
    }

    public Usuario(String nombre, int edad, String correo, String contrasena,
                   String nacionalidad, String telefono, String tipoUsuario) {
        this.nombre = nombre;
        this.edad = edad;
        this.correo = correo;
        this.contrasena = contrasena;
        this.nacionalidad = nacionalidad;
        this.telefono = telefono;
        this.tipoUsuario = tipoUsuario;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    @Override
    public String toString() {
        return idUsuario + " | " + nombre + " | " + edad + " | " + correo
                + " | " + nacionalidad + " | " + telefono + " | " + tipoUsuario;
    }
}