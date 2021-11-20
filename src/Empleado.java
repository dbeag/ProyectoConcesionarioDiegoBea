import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Empleado {
    private String dni;
    private int idCategoria;
    private String nombre;
    private String apellidos;
    private int telefono;
    
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public int getIdCategoria() {
        return idCategoria;
    }
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public int getTelefono() {
        return telefono;
    }
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
    public void iniciarSesion(Connection con) {
        String sql;
        Statement ps;
        try {
            ps = con.createStatement();
            sql = "SELECT * FROM empleado where dni = \"" + dni.toUpperCase() + "\"";
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                this.setDni(rs.getString(1));
                this.setIdCategoria(rs.getInt(2));
                this.setNombre(rs.getString(3));
                this.setApellidos(rs.getString(4));
                this.setTelefono(rs.getInt(5));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    } 
}
